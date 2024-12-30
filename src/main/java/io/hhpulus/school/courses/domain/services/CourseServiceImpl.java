package io.hhpulus.school.courses.domain.services;

import io.hhpulus.school.courses.domain.Course;
import io.hhpulus.school.courses.domain.CourseRepository;
import io.hhpulus.school.courses.domain.CourseService;
import io.hhpulus.school.courses.domain.exceptions.CourseEnrollDisableException;
import io.hhpulus.school.courses.domain.exceptions.CourseNotFoundException;
import io.hhpulus.school.courses.domain.validations.CourseValidator;
import io.hhpulus.school.courses.infraStructure.applications.CourseMapper;
import io.hhpulus.school.courses.presentation.dtos.request.CreateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.request.UpdateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.response.CourseResponseDto;
import io.hhpulus.school.enrollments.domain.Enrollment;
import io.hhpulus.school.enrollments.domain.EnrollmentRepository;
import io.hhpulus.school.enrollments.presentation.dtos.EnrolledCourseResponseDto;
import io.hhpulus.school.enrollments.presentation.dtos.EnrollmentResponseDto;
import io.hhpulus.school.users.domain.User;
import io.hhpulus.school.users.domain.UserRepository;
import io.hhpulus.school.users.domain.exceptions.UserNotFoundException;
import io.hhpulus.school.users.infraStructure.application.UserMapper;
import io.hhpulus.school.users.presentation.dtos.UserResponseDto;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.hhpulus.school.courses.domain.constants.CourseConstants.MAXIMUM_COURSE_STUDENTS;
import static io.hhpulus.school.courses.domain.exceptions.CourseErrorMessage.*;
import static io.hhpulus.school.users.domain.exceptions.UserErrorMessages.USER_ID_DOES_NOT_EXISTS;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    public CourseServiceImpl(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public CourseResponseDto createCourse(CreateCourseRequestDto requestDto) {
        return courseRepository.create(requestDto);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public int applyCourse(long userId, long courseId) {
        // 유효한 유저인지 검증
        User user = this.isAvailableUser(userId);

        // 유효한 강의인지 검증 (행을잠근다)
        Course course = this.isAvailableCourse(courseId);

        // 유저가 이미 신청한 강의인지 확인
        this.isAlreadyEnrolledCourse(userId, courseId);

        // 현재 신청가능한 강의인지 확인
        this.isAvailableEnrollCourseNow(courseId);

        // 강의신청
        Enrollment enrollment = Enrollment.builder().course(course).user(user).build();
        this.enrollmentRepository.create(enrollment);

        // currentEnrollments  +1 업데이트
        UpdateCourseRequestDto requestDto = new UpdateCourseRequestDto(
                courseId,
                null,
                null,
                null,
                null,
                course.getCurrentEnrollments() + 1
        );
        CourseResponseDto result = this.updateCourseInfo(requestDto);
        return result.currentEnrollments();
    }

    @Transactional
    @Override
    public Page<CourseResponseDto> getEnableCourses(int page) {
        // 목적: 현재 신청 가능한 강좌 목록 조회
        // 유효성검사
        CourseValidator.checkPage(page);

        // enrollStartDate <= currentDate <= enrollEndDate 인지 확인
        Page<CourseResponseDto> list = this.courseRepository.getEnableEnrollCourses(LocalDate.now(), page);
        return list;
    }

    @Transactional
    @Override
    public CourseResponseDto updateCourseInfo(UpdateCourseRequestDto requestDto) {
        // 목적: 강좌정보 수정
        long courseId = requestDto.id();
        Course course = this.isAvailableCourse(courseId);

        // 부분수정할 수 있도록 course(기존데이터) 와 requestDto와 비교하여 update
        if(requestDto.name() != null) {
           course.setName(requestDto.name());
        }
        if(requestDto.lecturerName() != null) {
            course.setLecturerName(requestDto.lecturerName());
        }
        if(requestDto.enrollStartDate() != null) {
            course.setEnrollStartDate(requestDto.enrollStartDate());
        }
        if(requestDto.enrollEndDate() != null) {
            course.setEnrollEndDate(requestDto.enrollEndDate());
        }
        if(requestDto.currentEnrollments() != null) {
            course.setCurrentEnrollments(requestDto.currentEnrollments());
        }

        // 수정
        this.courseRepository.update(course);

        // 수정후 조회
        return CourseMapper.toResponseDto(course);

    }

    @Transactional
    @Override
    public List<EnrolledCourseResponseDto> getEnrollFinishedCourses(long userId) {
        // 목적: 특정 userId로 수강신청이 완료된 강좌 목록 조회
        // 유효성 검사
        isAvailableUser(userId);

        List<EnrolledCourseResponseDto> list = enrollmentRepository.getEnrolledCourses(userId);
        return list;
    }


    @Transactional
    @Override
    public void isAvailableEnrollCourseNow(long courseId) {
        // 목적: 현재 기준으로 신청이 가능한 강좌인지 확인
        // 오늘을 기준으로 신청이 불가능한가?
        Optional<CourseResponseDto> availableCourse = this.courseRepository.findEnableCourse(courseId, LocalDate.now());
        if(availableCourse.isEmpty()) {
            throw new CourseEnrollDisableException(COURSE_ENROLL_DISABLED);
        }
    }

    @Transactional
    @Override
    public Course isAvailableCourse(long courseId) {
        // 목적: 유효한 강의인지 확인
        // 유효성 검사
        CourseValidator.checkCourseId(courseId);

        // 존재한지
        Optional<CourseResponseDto> course = this.courseRepository.findById(courseId);

        if(course.isEmpty()) {
            throw new CourseNotFoundException(COURSE_ID_DOES_NOT_EXISTS);
        }
        return course.get().toEntity();
    }


    @Transactional
    @Override
    public User isAvailableUser(long userId) {
        // 목적: 유효한 유저인지 확인
        //유효성검사
        CourseValidator.checkUserId(userId);

        Optional<UserResponseDto> user = this.userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(USER_ID_DOES_NOT_EXISTS);
        }
        return user.get().toEntity();
    }

    @Transactional
    @Override
    public void isAlreadyEnrolledCourse(long userId, long courseId) {
        // 목적: 이미 신청이 완료된 강좌인지 확인

        // 이미 신청이 완료됐다면 enrollment 결과에 존재한다.
         Optional<EnrollmentResponseDto> enrollment = this.enrollmentRepository.findByUserIdAndCourseId(userId, courseId);

        if(enrollment.isPresent()) {
            throw new CourseEnrollDisableException(COURSE_ALREADY_ENROLLED);
        }
    }
}

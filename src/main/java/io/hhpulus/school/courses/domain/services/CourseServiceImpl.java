package io.hhpulus.school.courses.domain.services;

import io.hhpulus.school.courses.domain.Course;
import io.hhpulus.school.courses.domain.CourseRepository;
import io.hhpulus.school.courses.domain.CourseService;
import io.hhpulus.school.courses.domain.exceptions.CourseEnrollDisableException;
import io.hhpulus.school.courses.domain.exceptions.CourseNotFoundException;
import io.hhpulus.school.courses.domain.validations.CourseValidator;
import io.hhpulus.school.courses.presentation.dtos.request.CreateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.request.UpdateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.response.CourseResponseDto;
import io.hhpulus.school.enrollments.domain.EnrollmentRepository;
import io.hhpulus.school.enrollments.presentation.dtos.EnrolledCourseResponseDto;
import io.hhpulus.school.enrollments.presentation.dtos.EnrollmentResponseDto;
import io.hhpulus.school.users.domain.User;
import io.hhpulus.school.users.domain.UserRepository;
import io.hhpulus.school.users.domain.exceptions.UserNotFoundException;
import io.hhpulus.school.users.infraStructure.application.UserMapper;
import io.hhpulus.school.users.presentation.dtos.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    @Override
    public CourseResponseDto createCourse(CreateCourseRequestDto requestDto) {
        return courseRepository.create(requestDto);
    }

    @Override
    public void applyCourse(long userId, long courseId) {
        // 유저아이디 정보 조회
        this.isAvailableUser(userId);

        // 유저가 이미 신청한 강의인지 확인
        this.isAlreadyEnrolledCourse(userId, courseId);

        // 현재 신청가능한 강의인지 확인
        this.isAvailableEnrollCourseNow(courseId);

        // 강의신청
        User user = this.userRepository.findById(userId).get().toEntity();
        Course course = this.courseRepository.findById(courseId).get().toEntity();
        this.enrollmentRepository.create(user, course);
    }

    @Override
    public Page<CourseResponseDto> getEnableCourses(int page) {
        // 목적: 현재 신청 가능한 강좌 목록 조회
        // 유효성검사
        CourseValidator.checkPage(page);

        // enrollStartDate <= currentDate <= enrollEndDate 인지 확인
        Page<CourseResponseDto> list = this.courseRepository.getEnableEnrollCourses(LocalDate.now(), page);
        return list;
    }

    @Override
    public CourseResponseDto updateCourseInfo(UpdateCourseRequestDto requestDto) {
        // 목적: 강좌정보 수정
        long courseId = requestDto.id();
        this.isAvailableCourse(courseId);

        return this.courseRepository.update(requestDto);
    }

    @Override
    public List<EnrolledCourseResponseDto> getEnrollFinishedCourses(long userId) {
        // 목적: 특정 userId로 수강신청이 완료된 강좌 목록 조회
        // 유효성 검사
        isAvailableUser(userId);

        List<EnrolledCourseResponseDto> list = this.enrollmentRepository.getEnrolledCourses(userId);
        return list;
    }

    @Override
    public void isMaximumNumber(long courseId) {
        // 목적: 수강신청자 정원이 최대정원인지 확인
        // 유효성검사
        CourseValidator.checkCourseId(courseId);

        // todo
    }

    @Override
    public void isAvailableEnrollCourseNow(long courseId) {
        // 목적: 현재 기준으로 신청이 가능한 강좌인지 확인
        this.isAvailableCourse(courseId);

        // 오늘을 기준으로 신청이 가능한지 확인
        Optional<CourseResponseDto> course = this.courseRepository.findEnableCourse(courseId, LocalDate.now());
        if(course.isEmpty()) {
            throw new CourseEnrollDisableException(COURSE_ENROLL_DATE_EXPIRED);
        }

        // 강의 신청상태 확인
        boolean isEnableEnroll = course.get().enableEnroll();
        if (!isEnableEnroll) {
            throw new CourseEnrollDisableException(COURSE_ENROLL_DISABLED);
        }

        // 정원수가 최대정원인지 확인
        this.isMaximumNumber(courseId);
    }

    @Override
    public void isAvailableCourse(long courseId) {
        // 목적: 유효한 강의인지 확인
        // 유효성 검사
        CourseValidator.checkCourseId(courseId);

        // 존재한지
        Optional<CourseResponseDto> course = this.courseRepository.findById(courseId);
        if(course.isEmpty()) {
            throw new CourseNotFoundException(COURSE_ID_DOES_NOT_EXISTS);
        }
    }

    @Override
    public void isAvailableUser(long userId) {
        // 목적: 유효한 유저인지 확인
        //유효성검사
        CourseValidator.checkUserId(userId);

        Optional<UserResponseDto> user = this.userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(USER_ID_DOES_NOT_EXISTS);
        }
    }

    @Override
    public void isAlreadyEnrolledCourse(long userId, long courseId) {
        // 목적: 이미 신청이 완료된 강좌인지 확인
        // 유효성 검사
        CourseValidator.checkUserId(userId);
        CourseValidator.checkCourseId(courseId);

        // 이미 신청이 완료됐다면 enrollment 결과에 존재한다.
        Optional<EnrollmentResponseDto> enrollment = this.enrollmentRepository.findByUserIdAndCourseId(userId, courseId);
        if(enrollment.isPresent()) {
            throw new CourseEnrollDisableException(COURSE_ALREADY_ENROLLED);
        }
    }
}

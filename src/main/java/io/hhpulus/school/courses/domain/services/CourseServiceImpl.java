package io.hhpulus.school.courses.domain.services;

import io.hhpulus.school.courses.domain.CourseRepository;
import io.hhpulus.school.courses.domain.exceptions.CourseEnrollDisableException;
import io.hhpulus.school.courses.domain.validations.CourseValidator;
import io.hhpulus.school.courses.presentation.dtos.request.CreateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.request.UpdateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.response.CourseResponseDto;
import io.hhpulus.school.enrollments.domain.EnrollmentRepository;
import io.hhpulus.school.users.domain.UserRepository;
import io.hhpulus.school.users.domain.exceptions.UserNotFoundException;
import io.hhpulus.school.users.presentation.dtos.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.hhpulus.school.courses.domain.exceptions.CourseErrorMessage.COURSE_ID_DOES_NOT_EXISTS;
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
    public int applyCourse(long userId, long courseId) {
        // 유효성 검사
        CourseValidator.checkUserId(userId);
        CourseValidator.checkCourseId(courseId);

        // 유저아이디 정보 조회
        this.isAvailableUser(userId);

        // 현재 신청가능한 강의인지 확인
        this.isAvailableEnrollCourseNow(courseId);

        // 정원수가 최대정원인지 확인
        this.isMaximumNumber(courseId);

        // 유저가 이미등록된 강의인지 확인
        this.isAlreadyEnrolledCourse(userId, courseId);

        // 강의신청
        // todo


        // 현재 강의 신청자 리턴
        return 0;
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
        return this.courseRepository.update(requestDto);
    }

    @Override
    public Page<CourseResponseDto> getEnrollFinishedCourses(long userId) {
        // 목적: 특정 userId로 수강신청이 완료된 강좌 목록 조회
        // 유효성 검사
        CourseValidator.checkUserId(userId);



        // todo
        return null;
    }

    @Override
    public void isMaximumNumber(long courseId) {
        // 목적: 수강신청자 정원이 최대정원인지 확인
        // 유효성검사
        CourseValidator.checkCourseId(courseId);

        Optional<CourseResponseDto> course = this.courseRepository.findById(courseId);
        if(course.isEmpty()) {
            throw new CourseEnrollDisableException(COURSE_ID_DOES_NOT_EXISTS);
        }

        // todo
    }

    @Override
    public void isAvailableEnrollCourseNow(long courseId) {
        // 목적: 현재 신청이 가능한 강좌인지 확인
        // 유효성 검사
        CourseValidator.checkCourseId(courseId);

        Optional<CourseResponseDto> enableCourse = this.courseRepository.findEnableCourse(courseId, LocalDate.now());
        if(enableCourse.isEmpty()) {
            throw new CourseEnrollDisableException(COURSE_ID_DOES_NOT_EXISTS);
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

        // todo

    }
}

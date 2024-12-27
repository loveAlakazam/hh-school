package io.hhpulus.school.courses.domain;

import io.hhpulus.school.courses.presentation.dtos.request.CreateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.request.UpdateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.response.CourseResponseDto;
import io.hhpulus.school.enrollments.presentation.dtos.EnrolledCourseResponseDto;
import io.hhpulus.school.users.domain.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {
    CourseResponseDto createCourse(CreateCourseRequestDto requestDto); // 수강 생성

    // 수강 신청
    void applyCourse(long userId, long courseId); // 특강신청 (선착순 신청)

    // 현재 신청가능 강좌목록 조회 (offset pagination)
    Page<CourseResponseDto> getEnableCourses(int page);

    // 수강신청 정보 수정
    CourseResponseDto updateCourseInfo(UpdateCourseRequestDto requestDto);

    // 특정 userId로 수강신청이 완료된 강좌 목록 조회
    // todo: Pagination 을 적용하여 Page 로 리턴하도록 리팩터링
    List<EnrolledCourseResponseDto> getEnrollFinishedCourses(long userId);

    // 수강신청자 정원이 최대정원인지 확인
    void isMaximumNumber(long courseId);

    // 현재날짜를 기준으로 신청이 가능한 강좌인지
    void isAvailableEnrollCourseNow(long courseId);

    // 유효 강좌 검증
    void isAvailableCourse(long courseId);

    // 유효 유저 검증
    void isAvailableUser(long userId);

    // 이미 신청완료된 강죄인지
    void isAlreadyEnrolledCourse(long userId, long courseId);

}
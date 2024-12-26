package io.hhpulus.school.courses.domain.services;

import io.hhpulus.school.courses.presentation.dtos.request.CreateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.request.UpdateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.response.CourseResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CourseService {
    CourseResponseDto createCourse(CreateCourseRequestDto requestDto); // 수강 생성

    // 수강 신청
    // 리턴타입이 int 이유: 현재 수강신청학생수
    int applyCourse(long userId, long courseId); // 특강신청 (선착순 신청)


    // 현재 신청가능 강좌목록 조회 (offset pagination)
    Page<CourseResponseDto> getEnableCourses(int page);

    // 수강신청 정보 수정
    CourseResponseDto updateCourseInfo(UpdateCourseRequestDto requestDto);

    // 특정 userId로 수강신청이 완료된 강좌 목록 조회
    Page<CourseResponseDto> getEnrollFinishedCourses(long userId);

    // 수강신청자 정원이 최대정원인지 확인
    void isMaximumNumber(long courseId);

    // 현재날짜를 기준으로 신청이 가능한 강좌인지
    void isAvailableEnrollCourseNow(long courseId);

    // 유효유저인지 검증
    void isAvailableUser(long userId);

    // 이미 신청완료된 강죄인지
    void isAlreadyEnrolledCourse(long userId, long courseId);

}
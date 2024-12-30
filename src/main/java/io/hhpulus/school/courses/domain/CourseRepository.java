package io.hhpulus.school.courses.domain;

import io.hhpulus.school.courses.presentation.dtos.request.CreateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.request.UpdateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.response.CourseResponseDto;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;


public interface CourseRepository {
    // 강좌 등록
    CourseResponseDto create(CreateCourseRequestDto requestDto);

    // 강좌 정보 수정
    void update(Course updatedCourse);

    // 단건 강좌 조회
    Optional<CourseResponseDto> findById(long id);


    // 단건의 신청가능한 강좌 조회
    Optional<CourseResponseDto> findEnableCourse(long id, LocalDate currentDate);

    // 신청가능한 강좌목록 조회
    Page<CourseResponseDto> getEnableEnrollCourses (LocalDate currentDate, int page);

    Course save(Course course);

    void deleteAll();
}

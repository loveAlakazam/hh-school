package io.hhpulus.school.courses.presentation.dtos.request;

import io.hhpulus.school.courses.domain.Course;
import io.hhpulus.school.courses.domain.validations.CourseValidator;
import lombok.Builder;

import java.time.LocalDate;

import static io.hhpulus.school.courses.domain.Course.DEFAULT_DAYS;

public record CreateCourseRequestDto (
        String name,
        String lecturerName,
        LocalDate enrollStartDate,
        LocalDate enrollEndDate
){
    @Builder
    public CreateCourseRequestDto(String name, String lecturerName, LocalDate enrollStartDate, LocalDate enrollEndDate) {
        // 기본값 셋팅
        this.enrollStartDate = enrollStartDate == null ? LocalDate.now() : enrollStartDate;
        this.enrollEndDate = enrollEndDate == null ? this.enrollStartDate.plusDays(DEFAULT_DAYS) : enrollEndDate;

        // 호출전에 유효성검사
        CourseValidator.checkCourseName(name); // 강좌명
        CourseValidator.checkLecturerName(lecturerName); // 강연자명
        this.name = name;
        this.lecturerName = lecturerName;
    }

    // CreateCourseRequestDto -> Course 엔티티
    public Course toEntity() {
        Course course = Course.builder()
                .name(name)
                .lecturerName(lecturerName)
                .enrollStartDate(enrollStartDate)
                .enrollEndDate(enrollEndDate)
                .build();
        return course;
    }
}

package io.hhpulus.school.courses.presentation.dtos.request;

import io.hhpulus.school.courses.domain.Course;
import io.hhpulus.school.courses.domain.validations.CourseValidator;

import java.time.LocalDate;

import static io.hhpulus.school.courses.domain.Course.DEFAULT_DAYS;

public record CreateCourseRequestDto (
        String name,
        String lecturerName,
        LocalDate enrollStartDate,
        int days
){
    // Compact Constructor - 생성자 호출이전에 유효성 검사
    public CreateCourseRequestDto {
        CourseValidator.checkCourseName(name); // 강좌명
        CourseValidator.checkLecturerName(lecturerName); // 강연자명
        CourseValidator.checkEnrollStartDate(enrollStartDate); // 신청시작날짜
        CourseValidator.checkDays(days); // 수강신청기간
    }

    // 기본생성자
    public CreateCourseRequestDto(String name, String lecturerName, LocalDate enrollStartDate) {
        this(name, lecturerName, enrollStartDate, DEFAULT_DAYS);
    }

    // CreateCourseRequestDto -> Course 엔티티
    public Course toEntity() {
        Course course = new Course();
        course.setName(this.name);
        course.setLecturerName(this.lecturerName);
        course.setEnrollStartDate(this.enrollStartDate);
        course.setDays(this.days);
        course.setEnrollEndDate(this.enrollStartDate, this.days);
        return course;
    }
}

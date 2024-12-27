package io.hhpulus.school.courses.presentation.dtos.response;


import io.hhpulus.school.courses.domain.Course;

import java.time.LocalDate;

// read-only
public record CourseResponseDto(
        long id, // 아이디
        String name, // 강의명
        String lecturerName, // 강사명
        boolean enableEnroll, // 수강신청가능 여부
        LocalDate enrollStartDate, // 수강신청 시작날짜
        LocalDate enrollEndDate // 수강신청 종료날짜
) {

    public Course toEntity() {
        Course course = new Course();
        course.setId(id);
        course.setName(this.name);
        course.setLecturerName(this.lecturerName);
        course.setEnrollStartDate(this.enrollStartDate);
        course.setEnrollEndDate(this.enrollStartDate);
        return course;
    }
}

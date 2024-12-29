package io.hhpulus.school.courses.presentation.dtos.response;


import io.hhpulus.school.courses.domain.Course;

import java.time.LocalDate;

// read-only
public record CourseResponseDto(
        long id, // 아이디

        String name, // 강의명
        String lecturerName, // 강사명
        LocalDate enrollStartDate, // 수강신청 시작날짜
        LocalDate enrollEndDate, // 수강신청 종료날짜
        Integer currentEnrollments // 현재 신청자 수
) {

    public Course toEntity() {
        Course course = Course.builder()
                .id(id)
                .name(name)
                .lecturerName(lecturerName)
                .enrollStartDate(enrollStartDate)
                .enrollEndDate(enrollEndDate)
                .currentEnrollments(currentEnrollments)
                .build();

        return course;
    }
}

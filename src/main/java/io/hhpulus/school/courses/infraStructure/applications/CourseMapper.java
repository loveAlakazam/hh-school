package io.hhpulus.school.courses.infraStructure.applications;

import io.hhpulus.school.courses.domain.Course;
import io.hhpulus.school.courses.presentation.dtos.response.CourseResponseDto;

import java.time.LocalDate;

public class CourseMapper {
    // entity Course -> CourseResponseDto 로 변환
    public static CourseResponseDto toResponseDto(Course course) {
        if(course == null) {
            return null;
        }

        long id = course.getId();
        String name = course.getName();
        String lecturerName = course.getLecturerName();
        boolean enableEnroll = course.isEnableEnroll();
        LocalDate enrollStartDate = course.getEnrollStartDate();
        LocalDate enrollEndDate = course.getEnrollEndDate();
        return new CourseResponseDto(id, name, lecturerName, enableEnroll, enrollStartDate, enrollEndDate);
    }
}

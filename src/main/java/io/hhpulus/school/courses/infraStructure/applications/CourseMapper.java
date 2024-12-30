package io.hhpulus.school.courses.infraStructure.applications;

import io.hhpulus.school.courses.domain.Course;
import io.hhpulus.school.courses.domain.exceptions.CourseNotFoundException;
import io.hhpulus.school.courses.presentation.dtos.response.CourseResponseDto;

import java.time.LocalDate;

import static io.hhpulus.school.courses.domain.exceptions.CourseErrorMessage.COURSE_NOT_FOUND;

public class CourseMapper {
    // entity Course -> CourseResponseDto 로 변환
    public static CourseResponseDto toResponseDto(Course course) {
        if(course == null)
            throw new CourseNotFoundException(COURSE_NOT_FOUND);

        long id = course.getId();
        String name = course.getName();
        String lecturerName = course.getLecturerName();
        LocalDate enrollStartDate = course.getEnrollStartDate();
        LocalDate enrollEndDate = course.getEnrollEndDate();
        int currentEnrollments = course.getCurrentEnrollments();

        return new CourseResponseDto(
            id,
            name,
            lecturerName,
            enrollStartDate,
            enrollEndDate,
            currentEnrollments
        );
    }
}

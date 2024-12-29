package io.hhpulus.school.enrollments.presentation.dtos;

import java.time.LocalDateTime;

public  record EnrolledCourseResponseDto(
    long courseId,
    String courseName,
    String lecturerName,
    LocalDateTime createdAt
){
}

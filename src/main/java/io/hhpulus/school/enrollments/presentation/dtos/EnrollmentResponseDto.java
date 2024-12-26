package io.hhpulus.school.enrollments.presentation.dtos;

public record EnrollmentResponseDto(
        long id,
        long userId,
        long courseId
) {
}

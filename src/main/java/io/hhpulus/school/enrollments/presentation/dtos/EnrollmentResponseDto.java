package io.hhpulus.school.enrollments.presentation.dtos;

import io.hhpulus.school.enrollments.domain.Enrollment;

public record EnrollmentResponseDto (
        long id,
        long userId,
        long courseId
){


}

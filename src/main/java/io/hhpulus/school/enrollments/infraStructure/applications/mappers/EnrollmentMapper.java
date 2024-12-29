package io.hhpulus.school.enrollments.infraStructure.applications.mappers;

import io.hhpulus.school.enrollments.domain.Enrollment;
import io.hhpulus.school.enrollments.presentation.dtos.EnrollmentResponseDto;

public class EnrollmentMapper {
    // Enrollment 엔티티 -> EnrollmentResponseDto 로 변환
    public static EnrollmentResponseDto toResponseDto(Enrollment enrollment) {
        long id;
        long userId;
        long courseId;

        id = enrollment.getId();
        userId = enrollment.getUser().getId();
        courseId = enrollment.getCourse().getId();
        return new EnrollmentResponseDto(id, userId, courseId);
    }
}

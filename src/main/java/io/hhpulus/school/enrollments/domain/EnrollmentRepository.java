package io.hhpulus.school.enrollments.domain;

import io.hhpulus.school.courses.domain.Course;
import io.hhpulus.school.enrollments.presentation.dtos.EnrolledCourseResponseDto;
import io.hhpulus.school.enrollments.presentation.dtos.EnrollmentResponseDto;
import io.hhpulus.school.users.domain.User;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository {
    // 수강신청 등록
    public void create(Enrollment enrollment);

    // 수강신청됐는지 확인
    Optional<EnrollmentResponseDto> findByUserIdAndCourseId(long userId, long courseId);

    // 락을 활용하여 수강신청됐는지확인
    Optional<EnrollmentResponseDto> findByUserIdAndCourseIdWithLock(long userId, long courseId);

    Optional<Enrollment> findByIdWithLock(long enrollmentId);

    // 특정 userId로 수강신청 완료된 강의 목록 조회
    List<EnrolledCourseResponseDto> getEnrolledCourses(long userId);

    void deleteAll();
}

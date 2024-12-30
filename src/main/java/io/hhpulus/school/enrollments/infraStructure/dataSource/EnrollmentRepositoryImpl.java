package io.hhpulus.school.enrollments.infraStructure.dataSource;

import io.hhpulus.school.courses.infraStructure.dataSource.EnrolledCourseProjection;
import io.hhpulus.school.enrollments.domain.Enrollment;
import io.hhpulus.school.enrollments.domain.EnrollmentRepository;
import io.hhpulus.school.enrollments.infraStructure.applications.mappers.EnrollmentMapper;
import io.hhpulus.school.enrollments.presentation.dtos.EnrolledCourseResponseDto;
import io.hhpulus.school.enrollments.presentation.dtos.EnrollmentResponseDto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class EnrollmentRepositoryImpl implements EnrollmentRepository {
    private final EnrollmentORMRepository enrollmentORMRepository;

    public EnrollmentRepositoryImpl(EnrollmentORMRepository enrollmentORMRepository) {
        this.enrollmentORMRepository = enrollmentORMRepository;
    }

    // 수강신청
    @Override
    public void create(Enrollment enrollment) {
        this.enrollmentORMRepository.save(enrollment);
    }


    @Override
    public Optional<EnrollmentResponseDto> findByUserIdAndCourseId(long userId, long courseId) {
        return this.enrollmentORMRepository.findEnrollmentByUserIDAndCourseId(userId, courseId).map(EnrollmentMapper::toResponseDto);
    }


    // 수강신청 완료 강의 목록 조회
    @Override
    public List<EnrolledCourseResponseDto> getEnrolledCourses(long userId) {
        List<EnrolledCourseProjection> projections = enrollmentORMRepository.getEnrolledCourses(userId);

        return projections.stream().map(p -> new EnrolledCourseResponseDto(
                p.getCourseId(),
                p.getCourseName(),
                p.getLecturerName(),
                p.getCreatedAt()
        )).toList();
    }

    @Override
    public void deleteAll() {
        enrollmentORMRepository.deleteAll();
    }
}

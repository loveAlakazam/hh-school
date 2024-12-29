package io.hhpulus.school.enrollments.infraStructure.dataSource;

import io.hhpulus.school.courses.infraStructure.dataSource.EnrolledCourseProjection;
import io.hhpulus.school.enrollments.domain.Enrollment;
import io.hhpulus.school.enrollments.domain.EnrollmentRepository;
import io.hhpulus.school.enrollments.infraStructure.applications.mappers.EnrollmentMapper;
import io.hhpulus.school.enrollments.presentation.dtos.EnrolledCourseResponseDto;
import io.hhpulus.school.enrollments.presentation.dtos.EnrollmentResponseDto;
import org.springframework.stereotype.Repository;

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


    // userId와 courseId 로 enrollment 단건 조회
    // 한사람이 동일한 강의를 중복으로 듣는것을 막고자,  (userId, courseId) 를 enrollment 의 복합키로 하였습니다.
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
}

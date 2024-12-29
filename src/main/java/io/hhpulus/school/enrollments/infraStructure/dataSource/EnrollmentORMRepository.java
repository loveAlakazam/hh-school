package io.hhpulus.school.enrollments.infraStructure.dataSource;

import io.hhpulus.school.courses.infraStructure.dataSource.EnrolledCourseProjection;
import io.hhpulus.school.enrollments.domain.Enrollment;
import io.hhpulus.school.enrollments.presentation.dtos.EnrolledCourseResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentORMRepository extends JpaRepository<Enrollment, Long> {

    // 특정 userId로 수강신청 완료된 강좌 조회
    @Query(value=
            "SELECT " +
                "c.id AS courseId, " +
                "c.name AS courseName, " +
                "c.lecturerName AS lecturerName, " +
                "e.createdAt AS createdAt " +
            "FROM Enrollment e " +
                    "JOIN e.user u " +
                    "JOIN e.course c " +
            "WHERE u.id = :userId "
    )
    List<EnrolledCourseProjection> getEnrolledCourses(@Param("userId") long userId);


    // userId, courseId 로 단건의 enrollment 조회
    @Query(
            "SELECT e " +
            "FROM Enrollment e " +
                    "JOIN e.user u " +
                    "JOIN e.course c " +
            "WHERE u.id = :userId " +
                    "AND c.id = :courseId "
    )
    Optional<Enrollment> findEnrollmentByUserIDAndCourseId(@Param("userId") long userId, @Param("courseId") long courseId);

}

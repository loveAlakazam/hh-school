package io.hhpulus.school.enrollments.infraStructure.dataSource;

import io.hhpulus.school.enrollments.domain.Enrollment;
import io.hhpulus.school.enrollments.presentation.dtos.EnrolledCourseResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentORMRepository extends JpaRepository<Enrollment, Long> {

    // 특정 userId로 수강신청 완료된 강좌 조회
    @Query(
            "SELECT new io.hhpulus.school.enrollments.presentation.dtos.EnrolledCourseResponseDto(c.id, c.name, c.lecturer_name, e.created_at)" +
            "FROM Enrollment e" +
                "JOIN FETCH e.user u" +
                "JOIN FETCH e.course c" +
            "WHERE u.id = :userId"
    )
    List<EnrolledCourseResponseDto> getEnrolledCourses(@Param("userId") long userId);


    @Query(
            "SELECT e" +
            "FROM Enrollment e" +
                    "JOIN FETCH e.user u" +
                    "JOIN FETCH e.course c" +
            "WHERE u.id = :userId" +
                    "AND c.id = :courseId"
    )
    Optional<Enrollment> findEnrollmentByUserIDAndCourseId(@Param("userId") long userId, @Param("courseId") long courseId);

}

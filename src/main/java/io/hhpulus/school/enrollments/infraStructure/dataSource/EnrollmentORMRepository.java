package io.hhpulus.school.enrollments.infraStructure.dataSource;

import io.hhpulus.school.courses.infraStructure.dataSource.EnrolledCourseProjection;
import io.hhpulus.school.enrollments.domain.Enrollment;
import io.hhpulus.school.enrollments.presentation.dtos.EnrolledCourseResponseDto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
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


    // applyCourse의  isAlreadyEnrollmentCourse 에 있음.
    // 비관적락을 사용하여 데이터를 읽는순간부터 락을 걸어서 다른 스레드의 읽기/쓰기 트랜잭션을 수행하지 못하도록 막는다.
    // userId, courseId 로 단건의 enrollment 조회
    @Lock(LockModeType.PESSIMISTIC_WRITE)
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

package io.hhpulus.school.courses.infraStructure.dataSource;

import io.hhpulus.school.courses.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface CourseORMRepository extends JpaRepository<Course,Long> {
    @Query("SELECT c FROM Course c" +
            "WHERE :currentDate BETWEEN c.enroll_start_date AND c.enroll_end_date" +
            "AND c.enable_enroll = true"
    )
    Page<Course> findEnableEnrollCourses(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    @Query("SELECT c FROM Course c" +
            "WHERE :currentDate BETWEEN c.enroll_start_date AND c.enroll_end_date" +
            "AND c.enable_enroll = true" +
            "AND c.id = :courseId"
    )
    Optional<Course> findEnableEnrollCourse(@Param("courseId") long courseId, @Param("currentDate") LocalDate currentDate);
}

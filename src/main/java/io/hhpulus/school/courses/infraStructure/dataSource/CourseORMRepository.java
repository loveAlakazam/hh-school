package io.hhpulus.school.courses.infraStructure.dataSource;

import io.hhpulus.school.courses.domain.Course;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

import static io.hhpulus.school.courses.domain.constants.CourseConstants.MAXIMUM_COURSE_STUDENTS;

public interface CourseORMRepository extends JpaRepository<Course,Long> {
    @Query("SELECT c FROM Course c " +
            " WHERE :currentDate BETWEEN c.enrollStartDate AND c.enrollEndDate " +
            " AND c.currentEnrollments < " + MAXIMUM_COURSE_STUDENTS  // 수강신청 30명 미만
    )
    Page<Course> findEnableEnrollCourses(@Param("currentDate") LocalDate currentDate, Pageable pageable );

    @Query(" SELECT c FROM Course c " +
            " WHERE :currentDate BETWEEN c.enrollStartDate AND c.enrollEndDate " +
            " AND c.currentEnrollments < " + MAXIMUM_COURSE_STUDENTS + // 수강신청인원 30명 미만
            " AND c.id = :courseId"
    )
    Optional<Course> findEnableEnrollCourse(@Param("courseId") long courseId, @Param("currentDate") LocalDate currentDate);

    @Transactional
    @Modifying
    @Query(" UPDATE Course c SET " +
        " c.name = :name ," +
        " c.lecturerName = COALESCE( :lecturerName, c.name ), " +
        " c.enrollStartDate = COALESCE( :enrollStartDate, c.enrollStartDate ) ," +
        " c.enrollEndDate = COALESCE( :enrollEndDate, c.enrollEndDate ), " +
        " c.currentEnrollments = COALESCE( :currentEnrollments, c.currentEnrollments ) " +
        "WHERE c.id = :id"
    )
    int updateInfo(@Param("id") long id,
      @Param("name") String name,
      @Param("lecturerName") String lecturerName,
      @Param("enrollStartDate") LocalDate enrollStartDate,
      @Param("enrollEndDate") LocalDate enrollEndDate,
      @Param("currentEnrollments") int currentEnrollments
    );
}

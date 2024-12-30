package io.hhpulus.school.courses.infraStructure.dataSource;

import io.hhpulus.school.courses.domain.Course;
import io.hhpulus.school.courses.presentation.dtos.response.CourseResponseDto;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
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

    // applyCourse에서 isAvailableEnrollCourseNow 에 해당 조회를 막는다.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(" SELECT c FROM Course c " +
            " WHERE :currentDate BETWEEN c.enrollStartDate AND c.enrollEndDate " +
            " AND c.currentEnrollments < " + MAXIMUM_COURSE_STUDENTS + // 수강신청인원 30명 미만
            " AND c.id = :courseId"
    )
    Optional<Course> findEnableEnrollCourse(@Param("courseId") long courseId, @Param("currentDate") LocalDate currentDate);


    // applyCourse 의 currentEnrollments 업데이트
    @Lock(LockModeType.PESSIMISTIC_WRITE)
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

    // 비관적 락을 사용하여 다른 트랜잭션이 읽기/쓰기 접근을 못하도록 막는다.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Course c WHERE c.id = :id")
    Optional<Course> findByIdWithLock(@Param("id") long id);
}

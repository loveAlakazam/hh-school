package io.hhplus.school.courses.domain;

import io.hhplus.school.users.domain.UserServiceTest;
import io.hhpulus.school.HHPlusSchoolMainApplication;
import io.hhpulus.school.courses.domain.Course;
import io.hhpulus.school.courses.domain.CourseRepository;
import io.hhpulus.school.courses.domain.CourseService;
import io.hhpulus.school.courses.domain.services.CourseServiceImpl;
import io.hhpulus.school.courses.presentation.dtos.request.CreateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.response.CourseResponseDto;
import io.hhpulus.school.enrollments.domain.Enrollment;
import io.hhpulus.school.enrollments.domain.EnrollmentRepository;
import io.hhpulus.school.enrollments.presentation.dtos.EnrollmentResponseDto;
import io.hhpulus.school.users.domain.User;
import io.hhpulus.school.users.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static io.hhpulus.school.courses.domain.constants.CourseConstants.MAXIMUM_COURSE_STUDENTS;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = HHPlusSchoolMainApplication.class) // Spring Boot 테스트 컨텍스트 사용
public class CourseServiceConcurrencyTest {
    @Autowired
    private CourseService courseService;

    @Autowired
    private UserRepository userRepository; // Mock 객체
    @Autowired
    private CourseRepository courseRepository; // Mock 객체
    @Autowired
    private EnrollmentRepository enrollmentRepository; // Mock 객체

    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    private int USER_THREAD_POOL_COUNT = 40;
    private List<Long> userIds;
    private long courseId = 1L;


    @BeforeEach
    void setUp() {
        enrollmentRepository.deleteAll();
        userRepository.deleteAll();
        courseRepository.deleteAll();
        userIds = new ArrayList<>();
    }
    @AfterEach
    void tearDown() {
        userIds.clear();
    }


    private void createTestCourse(int maxCapacity) {
        CreateCourseRequestDto courseRequestDto = CreateCourseRequestDto
                .builder()
                .name("Test Course")
                .lecturerName("Test Lecturer")
                .enrollStartDate(LocalDate.now())
                .enrollEndDate(LocalDate.now().plusDays(7))
                .build();
        CourseResponseDto course = courseRepository.create(courseRequestDto);
        assertNotNull(course.id());
    }

    private void createTestUsers(int count) {
        for (int i = 0; i < count; i++) {
            User user = User.builder()
                    .name("User " + i)
                    .build();
            user = userRepository.save(user);
            assertNotNull(user.getId());
            userIds.add(user.getId());
        }
    }

    @Test
    @DisplayName("40명의 인원이 1개의 강의를 동시에 신청할 때, 선착순 30명만 신청된다.")
    void testConcurrentEnrollmentsOnlyAllowingFirst30() throws InterruptedException {
        // given
        int numberOfThreads = USER_THREAD_POOL_COUNT;
        // 유저 데이터 40개 생성
        createTestUsers(numberOfThreads);

        // 정원 30명짜리 강의 1개 생성
        createTestCourse(MAXIMUM_COURSE_STUDENTS);

        CountDownLatch latch = new CountDownLatch(numberOfThreads); // 40개의 스레드풀 생성
        AtomicInteger successCount = new AtomicInteger(0); // 성공카운트
        AtomicInteger failCount = new AtomicInteger(0); // 실패카운트

        // when
        // 비동기 요청시작
//        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
//        for(int i=0 ; i< 40; i++) {
//            long userId = (i + 1);
//            executor.submit(() -> {
//                try {
//                    courseService.applyCourse(userId, courseId); // 독립된 트랜잭션
//                    successCount.incrementAndGet();
//                } catch (Exception e) {
//                    failCount.incrementAndGet();
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }



        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        for (long userId : userIds) {
            executor.execute(() -> {
                try {
                    courseService.applyCourse(userId, courseId); // 독립된 트랜잭션
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    // 실패 시 무시
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await(); // 모든 쓰레드가 끝날 때까지 대기
        executor.shutdown(); // 작업완료후 ExecutorService 를 종료한다.

        // then
        assertEquals(MAXIMUM_COURSE_STUDENTS, successCount.get(), "유저 30명은 성공한다.");
        assertEquals(numberOfThreads - MAXIMUM_COURSE_STUDENTS, failCount.get(), "유저 10명은 실패한다");
        CourseResponseDto course = courseRepository.findById(courseId).orElseThrow();
        assertEquals(MAXIMUM_COURSE_STUDENTS, course.currentEnrollments(), "강의신청은 30명만 가능하다");
    }

    @Test
    @DisplayName("동일한 유저가 동일강의를 5번 수강신청을 요청했을 때 딱 한번만 성공한다")
    void testSameUserCannotEnrollMultipleTimes() throws InterruptedException {
        // Given
        // 유저 데이터 1개 생성
        createTestUsers(1);

        // 정원 30명짜리 강의 1개 생성
        createTestCourse(MAXIMUM_COURSE_STUDENTS);
        long userId = 1L; // 1번유저
        CountDownLatch latch = new CountDownLatch(5); // 동일한 유저가 5번 요청


        AtomicInteger successCount = new AtomicInteger(0); // 성공카운트
        AtomicInteger failCount = new AtomicInteger(0); // 실패카운트

        // When
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                try {
                    courseService.applyCourse(userId, courseId);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    // 실패 시 무시
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await(); // 모든 쓰레드가 끝날 때까지 대기
        executor.shutdown();

        // Then
        assertEquals(1, successCount.get(), "한번만 수강신청을 성공한다");
        assertEquals(4, failCount.get(), "이미 수강신청을했으므로 4번 실패한다");
        EnrollmentResponseDto enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId).orElse(null);
        assertNotNull(enrollment, "수강신청 데이터는 1개만 생성된다");
    }
}

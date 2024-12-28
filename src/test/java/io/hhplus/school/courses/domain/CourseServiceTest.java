package io.hhplus.school.courses.domain;

import io.hhplus.school.users.domain.UserServiceTest;
import io.hhpulus.school.courses.domain.CourseRepository;
import io.hhpulus.school.courses.domain.CourseService;
import io.hhpulus.school.courses.domain.exceptions.CourseNotFoundException;
import io.hhpulus.school.courses.domain.services.CourseServiceImpl;
import io.hhpulus.school.enrollments.domain.EnrollmentRepository;
import io.hhpulus.school.users.domain.UserRepository;
import io.hhpulus.school.users.domain.exceptions.UserNotFoundException;
import io.hhpulus.school.users.domain.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Optional;

import static io.hhpulus.school.courses.domain.exceptions.CourseErrorMessage.COURSE_ID_DOES_NOT_EXISTS;
import static io.hhpulus.school.courses.domain.validations.CourseValidator.*;
import static io.hhpulus.school.users.domain.exceptions.UserErrorMessages.USER_ID_DOES_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private UserRepository userRepository; // Mock 객체
    @Mock
    private CourseRepository courseRepository; // Mock 객체
    @Mock
    private EnrollmentRepository enrollmentRepository; // Mock 객체


    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        courseRepository = Mockito.mock(CourseRepository.class);
        enrollmentRepository = Mockito.mock(EnrollmentRepository.class);

        courseService = new CourseServiceImpl(courseRepository, enrollmentRepository, userRepository);
    }

    @Nested
    public class CourseValidator_Failure_UnitTests {
        @Test
        @DisplayName("userId 가 음수면 IllegalArgumentException 예외를 발생한다.")
        public void checkUserId_shouldThrow_IllegalArgumentException() {
            // given
            long userId = -1L;

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkUserId(userId));

            // then
            assertEquals(USER_ID_POSITIVE_NUMBER_VALIDATION_POLICY, exception.getMessage());
        }
        @Test
        @DisplayName("courseId 가 음수면  IllegalArgumentException 예외를 발생한다.")
        public void checkCourseId_shouldThrow_IllegalArgumentException() {
            // given
            long courseId = -1L;

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkCourseId(courseId));

            // then
            assertEquals(COURSE_ID_POSITIVE_NUMBER_VALIDATION_POLICY, exception.getMessage());
        }
        @Test
        @DisplayName("courseName 이 화이트스페이스면 IllegalArgumentException 예외를 발생한다.")
        public void checkCourseName_WhiteSpace_shouldThrow_IllegalArgumentException() {
            // given
            String courseNameWhiteSpace = " ";

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkCourseName(courseNameWhiteSpace));

            // then
            assertEquals(COURSE_NAME_NOT_EMPTY_VALIDATION_POLICY, exception.getMessage());
        }
        @Test
        @DisplayName("courseName 이 아무것도입력되지 않으면 IllegalArgumentException 예외를 발생한다.")
        public void checkCourseName_EmptyString_shouldThrow_IllegalArgumentException() {
            // given
            String courseNameEmptyString = "";

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkCourseName(courseNameEmptyString));

            // then
            assertEquals(COURSE_NAME_NOT_EMPTY_VALIDATION_POLICY, exception.getMessage());
        }
        @Test
        @DisplayName("courseName 이 null 이면 IllegalArgumentException 예외를 발생한다.")
        public void checkCourseName_Null_shouldThrow_IllegalArgumentException() {
            // given
            String courseNameNull = null;

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkCourseName(courseNameNull));

            // then
            assertEquals(COURSE_NAME_NOT_EMPTY_VALIDATION_POLICY, exception.getMessage());
        }
        @Test
        @DisplayName("lecturerName 이 화이트스페이스면 IllegalArgumentException 예외를 발생한다.")
        public void checkLecturerName_WhiteSpace_shouldThrow_IllegalArgumentException() {
            // given
            String lecturerNameWhiteSpace = " ";

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkLecturerName(lecturerNameWhiteSpace));

            // then
            assertEquals(LECTURER_NAME_NOT_EMPTY_VALIDATION_POLICY, exception.getMessage());
        }
        @Test
        @DisplayName("lecturerName 이 아무것도입력되지 않으면 IllegalArgumentException 예외를 발생한다.")
        public void checkLecturerName_EmptyString_shouldThrow_IllegalArgumentException() {
            // given
            String lecturerNameEmptyString = "";

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkLecturerName(lecturerNameEmptyString));

            // then
            assertEquals(LECTURER_NAME_NOT_EMPTY_VALIDATION_POLICY, exception.getMessage());
        }
        @Test
        @DisplayName("lecturerName 이 null 이면 IllegalArgumentException 예외를 발생한다.")
        public void checkLecturerName_Null_shouldThrow_IllegalArgumentException() {
            // given
            String lecturerNameNull = null;

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkLecturerName(lecturerNameNull));

            // then
            assertEquals(LECTURER_NAME_NOT_EMPTY_VALIDATION_POLICY, exception.getMessage());
        }
        @Test
        @DisplayName("days 가 음수면  IllegalArgumentException 예외를 발생한다.")
        public void checkDays_NegativeNumber_shouldThrow_IllegalArgumentException() {
            // given
            int days = -1;

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkDays(days));

            // then
            assertEquals(DAYS_POSITIVE_NUMBER_VALIDATION_POLICY, exception.getMessage());
        }
        @Test
        @DisplayName("days 가 0이면  IllegalArgumentException 예외를 발생한다.")
        public void checkDays_Zero_shouldThrow_IllegalArgumentException() {
            // given
            int days = 0;

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkDays(days));

            // then
            assertEquals(DAYS_POSITIVE_NUMBER_VALIDATION_POLICY, exception.getMessage());
        }
        @Test
        @DisplayName("days > ENROLLMENT_MAXIMUM_DAYS 이면  IllegalArgumentException 예외를 발생한다.")
        public void checkDays_Over364_shouldThrow_IllegalArgumentException() {
            // given
            int days = 365;

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkDays(days));

            // then
            assertEquals(ENROLLMENT_DAYS_MAXIMUM_VALIDATION_POLICY, exception.getMessage());
        }
        @Test
        @DisplayName("enrollStartDate 가 null 이면  IllegalArgumentException 예외를 발생한다.")
        public void checkEnrollStartDate_shouldThrow_IllegalArgumentException() {
            // given
            LocalDate enrollStartDate = null;

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkEnrollStartDate(enrollStartDate));

            // then
            assertEquals(ENROLL_START_DATE_IS_NOT_NULL, exception.getMessage());
        }
        @Test
        @DisplayName("page 가 0 이면  IllegalArgumentException 예외를 발생한다.")
        public void checkPage_Zero_shouldThrow_IllegalArgumentException() {
            // given
            int page = 0;

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkPage(page));

            // then
            assertEquals(PAGE_POSITIVE_NUMBER_VALIDATION_POLICY, exception.getMessage());
        }
        @Test
        @DisplayName("page 가 음수이면  IllegalArgumentException 예외를 발생한다.")
        public void checkPage_NegativeNumber_shouldThrow_IllegalArgumentException() {
            // given
            int page = -1;

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkPage(page));

            // then
            assertEquals(PAGE_POSITIVE_NUMBER_VALIDATION_POLICY, exception.getMessage());
        }


    }

    @Nested
    public class CourseServiceUnitTest {
        @Test
        @DisplayName("courseId 가 존재하지 않으면 CourseNotFoundException 예외를 발생한다")
        public void isAvailableCourse_shouldThrow_CourseNotFoundException() {
            // given
            long courseId = 999L;

            // when
            CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> courseService.isAvailableCourse(courseId));

            // then
            assertEquals(COURSE_ID_DOES_NOT_EXISTS, exception.getMessage());
        }
        @Test
        @DisplayName("userId 가 존재하지 않으면 UserNotFoundException 예외를 발생한다.")
        public void isAvailableUser_shouldThrow_UserNotFoundException() {
            // given
            long userId = 999L; // 존재하지 않은 유저
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // when
            UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> courseService.isAvailableUser(userId));

            // then
            verify(userRepository, times(1)).findById(userId);
            assertEquals(USER_ID_DOES_NOT_EXISTS, exception.getMessage());
        }

    }


}

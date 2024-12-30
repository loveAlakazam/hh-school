package io.hhplus.school.courses.domain;

import io.hhplus.school.users.domain.UserServiceTest;
import io.hhpulus.school.courses.domain.Course;
import io.hhpulus.school.courses.domain.CourseRepository;
import io.hhpulus.school.courses.domain.CourseService;
import io.hhpulus.school.courses.domain.exceptions.CourseEnrollDisableException;
import io.hhpulus.school.courses.domain.exceptions.CourseNotFoundException;
import io.hhpulus.school.courses.domain.services.CourseServiceImpl;
import io.hhpulus.school.courses.domain.validations.CourseValidator;
import io.hhpulus.school.courses.infraStructure.applications.CourseMapper;
import io.hhpulus.school.courses.presentation.dtos.request.UpdateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.response.CourseResponseDto;
import io.hhpulus.school.enrollments.domain.Enrollment;
import io.hhpulus.school.enrollments.domain.EnrollmentRepository;
import io.hhpulus.school.enrollments.presentation.dtos.EnrolledCourseResponseDto;
import io.hhpulus.school.enrollments.presentation.dtos.EnrollmentResponseDto;
import io.hhpulus.school.users.domain.User;
import io.hhpulus.school.users.domain.UserRepository;
import io.hhpulus.school.users.domain.exceptions.UserNotFoundException;
import io.hhpulus.school.users.domain.services.UserService;
import io.hhpulus.school.users.presentation.dtos.UserResponseDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.hhpulus.school.courses.domain.constants.CourseConstants.*;
import static io.hhpulus.school.courses.domain.exceptions.CourseErrorMessage.*;
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
        @DisplayName("enrollEndDate 가 null 이면  IllegalArgumentException 예외를 발생한다.")
        public void checkEnrollEndDate_Null_ShouldThrow_IllegalArgumentException() {
            // given
            LocalDate enrollStartDate = LocalDate.now();
            LocalDate enrollEndDate = null;

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkEnrollEndDate(enrollStartDate, enrollEndDate));

            // then
            assertEquals(ENROLL_END_DATE_IS_NOT_NULL, exception.getMessage());
        }
        @Test
        @DisplayName("enrollEndDate 이 enrollStartDate 보다 과거면  IllegalArgumentException 예외를 발생한다.")
        public void checkEnrollEndDate_Past_Than_EnrollStartDate_ShouldThrow_IllegalArgumentException() {
            // given
            LocalDate enrollStartDate = LocalDate.now();
            LocalDate enrollEndDate = enrollStartDate.minusDays(1);

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> checkEnrollEndDate(enrollStartDate, enrollEndDate));

            // then
            assertEquals(ENROLL_END_DATE_LATER_THAN_ENROLL_START_DATE, exception.getMessage());
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

        @Test
        @DisplayName("이미 신청했다면 CourseEnrollDisableException 예외를 발생한다 : userId, courseId 가 매핑된 enrollment 데이터로우가 존재함.")
        public void isAlreadyEnrolledCourse_shouldThrow_CourseEnrollDisableException() {
            // given
            long userId = 1L;
            long courseId = 1L;
            long enrollmentId = 1L;
            EnrollmentResponseDto alreadyEnrolledEnrollment = new EnrollmentResponseDto(enrollmentId, userId, courseId);
            when(enrollmentRepository.findByUserIdAndCourseId(userId, courseId)).thenReturn(Optional.of(alreadyEnrolledEnrollment));

            // when
            CourseEnrollDisableException exception = assertThrows(CourseEnrollDisableException.class, () -> courseService.isAlreadyEnrolledCourse(userId, courseId));

            // then
            verify(enrollmentRepository,times(1)).findByUserIdAndCourseId(userId, courseId);
            assertEquals(COURSE_ALREADY_ENROLLED, exception.getMessage());
        }
        @Test
        @DisplayName("아직 신청하지 않았다면 성공한다 : userId, courseId 가 매핑된 enrollment 데이터로우가 없음")
        public void isAlreadyEnrolledCourse_should_success() {
            // given
            long userId = 1L;
            long courseId = 1L;
            when(enrollmentRepository.findByUserIdAndCourseId(userId, courseId)).thenReturn(Optional.empty());

            // when
            courseService.isAlreadyEnrolledCourse(userId, courseId);

            // then
            verify(enrollmentRepository,times(1)).findByUserIdAndCourseId(userId, courseId);
        }
        @Test
        @DisplayName("현재날짜가 수강 신청날짜(enrollStartDate)~수강신청 종료날짜(enrollEndDate) 사이가 아닌 날짜면 CourseEnrollDisableException 예외를 발생시킨다")
        public void isAvailableEnrollCourseNow_NotBetween_EnrollStartDate_And_EnrollEndDate_shouldThrows_CourseEnrollDisableException() {
            // given
            long courseId = 1L;
            CourseResponseDto course = new CourseResponseDto(
                    courseId,
                    "테스트 강좌명",
                    "테스트 강연자명",
                    LocalDate.of(2024, 12, 16), // error: 수강신청시작날짜: 2024-12-16
                    LocalDate.of(2024, 12, 23), // error: 수강신청종료날짜: 2024-12-23
                    0
            );
            when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
            when(courseRepository.findEnableCourse(courseId, LocalDate.now())).thenReturn(Optional.empty());

            // when
            CourseEnrollDisableException exception = assertThrows(CourseEnrollDisableException.class, () -> courseService.isAvailableEnrollCourseNow(courseId));

            // then
            verify(courseRepository, times(1)).findEnableCourse(courseId, LocalDate.now());
            assertEquals(COURSE_ENROLL_DISABLED, exception.getMessage());
        }

        @Test
        @DisplayName("수강신청인원이 " + MAXIMUM_COURSE_STUDENTS + "명 이상이면 CourseEnrollDisableException 예외를 발생시킨다")
        public void isAvailableEnrollCourseNow_Over30_shouldThrows_CourseEnrollDisableException() {

            // given
            long courseId = 1L;

            LocalDate now = LocalDate.now();
            CourseResponseDto course = new CourseResponseDto(
                    courseId,
                    "테스트 강좌명",
                    "테스트 강연자명",
                    now,  // 수강신청시작날짜: 현재날짜
                    now.plusDays(1), // 수강신청종료날짜: 현재날짜+1일
                    31 // error: 현재 신청인원수 > MAXIMUM_COURSE_STUDENTS
            );
            when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
            when(courseRepository.findEnableCourse(courseId, now)).thenReturn(Optional.empty());

            // when
            CourseEnrollDisableException exception = assertThrows(CourseEnrollDisableException.class, () -> courseService.isAvailableEnrollCourseNow(courseId));

            // then
            assertEquals(COURSE_ENROLL_DISABLED, exception.getMessage());
            verify(courseRepository, times(1)).findEnableCourse(courseId, now);
        }
        @Test
        @DisplayName("isAvailableEnrollCourseNow 를 성공한다")
        public void isAvailableEnrollCourseNow_shouldReturn_Success() {
            // given
            long courseId = 1L;
            LocalDate now = LocalDate.now();

            CourseResponseDto course = new CourseResponseDto(
                    courseId,
                    "테스트 강좌명",
                    "테스트 강연자명",
                    now,  // 수강신청시작날짜: 현재날짜
                    now.plusDays(1), // 수강신청종료날짜: 현재날짜+1일
                    25 // 현재 신청인원 수
            );
            when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
            when(courseRepository.findEnableCourse(courseId, now)).thenReturn(Optional.of(course));

            // when
            courseService.isAvailableEnrollCourseNow(courseId);

            // then
            verify(courseRepository, times(1)).findEnableCourse(courseId, now);
        }
        @Test
        @DisplayName("현재 신청가능한 강좌목록 1페이지 조회를 성공한다")
        public void getEnableCourses_PageOne_ShouldReturn_success() {
            // given
            int page = 1;
            int pageSize = DEFAULT_PAGINATION_SIZE; // 20
            LocalDate now = LocalDate.now();

            // 전체 강좌 셋팅: 26개
            List<CourseResponseDto> allCourses = new ArrayList<>();
            int steps = 26;
            for(int i = 1; i <= steps; i++) {
                long id= i;
                String name = "테스트 강좌 " + i;
                String lecturerName = "강연자 " + i;
                LocalDate enrollStartDate = now.minusDays(1);
                LocalDate enrollEndDate = now.plusDays(1);

                if( i % 5 == 0 ) {
                    // 5개는 신청불가능한 강의 : 신청가능한 기간에 속하지 않음
                    enrollStartDate = LocalDate.of(2024, 12, 24);
                    enrollEndDate = LocalDate.of(2024, 12, 25);
                }
                CourseResponseDto course = new CourseResponseDto(id, name, lecturerName, enrollStartDate, enrollEndDate, 0);
                allCourses.add(course);
            }

            // 신청가능한 강좌 필터링: 21개
            List<CourseResponseDto> filteredEnableCourses = allCourses.stream()
                    .filter(
                        // enrollStartDate <= now <= enrollEndDate 가 아니다.
                        course -> !now.isBefore(course.enrollStartDate()) && !now.isAfter(course.enrollEndDate())
                    )
                    .filter(
                        // currentEnrollment  < 30
                        course -> course.currentEnrollments() < 30
                    )
                    .collect(Collectors.toList());

            log.info("\n:: 필터링된 강좌 데이터개수 출력::" +  filteredEnableCourses.size());


            int start = Math.min((page - 1) * pageSize, filteredEnableCourses.size()); // 0
            int end = Math.min(start + pageSize, filteredEnableCourses.size()); // 20

            List<CourseResponseDto> enableCourses = filteredEnableCourses.subList(start, end);
            log.info("\n:: start 출력::" +  start);
            log.info("\n:: end 출력::" +  end);
            log.info("\n:: 1페이지에 나타낼 강좌 데이터개수 출력::" +  enableCourses.size());

            // 예상 1페이지 결과 데이터개수: 20개
            // 페이징당 최대 20 개의 데이터만 조회가능.
            Page < CourseResponseDto > expectedPageOne = new PageImpl<>(enableCourses, PageRequest.of(page, pageSize), filteredEnableCourses.size());
            when(courseRepository.getEnableEnrollCourses(eq(now), eq(page))).thenReturn(expectedPageOne);

            // when
            Page<CourseResponseDto> resultPageOne = courseService.getEnableCourses(page);

            // then
            assertNotNull(resultPageOne);
            assertEquals(20, resultPageOne.getContent().size()); // 1페이지 결과 데이터개수 확인
            assertEquals("테스트 강좌 1", resultPageOne.getContent().get(0).name()); // 맨첫번째 데이터 강좌명 비교
            assertEquals("강연자 2", resultPageOne.getContent().get(1).lecturerName()); // 두번째 데이터 강연자명 비교
            verify(courseRepository, times(1)).getEnableEnrollCourses(eq(now), eq(page));
        }
        @Test
        @DisplayName("현재 신청가능한 강좌목록 2페이지 조회를 성공한다")
        public void getEnableCourses_PageTwo_ShouldReturn_success() {
            // given
            int page = 2;
            int pageSize = DEFAULT_PAGINATION_SIZE; // 20
            LocalDate now = LocalDate.now();

            // 전체 강좌 셋팅: 27개
            List<CourseResponseDto> allCourses = new ArrayList<>();
            int steps = 27;
            for(int i = 1; i <= steps; i++) {
                long id= i;
                String name = "테스트 강좌 " + i;
                String lecturerName = "강연자 " + i;
                LocalDate enrollStartDate = now.minusDays(1);
                LocalDate enrollEndDate = now.plusDays(1);

                if( i % 5 == 0 ) {
                    // 5개는 신청불가능한 강의 : 신청가능한 기간에 속하지 않음
                    enrollStartDate = LocalDate.of(2024, 12, 24);
                    enrollEndDate = LocalDate.of(2024, 12, 25);
                }
                CourseResponseDto course = new CourseResponseDto(id, name, lecturerName, enrollStartDate, enrollEndDate, 0);
                allCourses.add(course);
            }

            // 신청가능한 강좌 필터링: 22개
            List<CourseResponseDto> filteredEnableCourses = allCourses.stream()
                    .filter(
                            // enrollStartDate <= now <= enrollEndDate 가 아니다.
                            course -> !now.isBefore(course.enrollStartDate()) && !now.isAfter(course.enrollEndDate())
                    )
                    .filter(
                            // currentEnrollment  < 30
                            course -> course.currentEnrollments() < 30
                    )
                    .collect(Collectors.toList());

            log.info("\n:: 필터링된 강좌 데이터개수 출력::" +  filteredEnableCourses.size());


            int start = Math.min((page - 1) * pageSize, filteredEnableCourses.size()); // 20
            int end = Math.min(start + pageSize, filteredEnableCourses.size()); // 22

            List<CourseResponseDto> enableCourses = filteredEnableCourses.subList(start, end);
            log.info("\n:: start 출력::" +  start);
            log.info("\n:: end 출력::" +  end);
            log.info("\n:: 2페이지에 나타낼 강좌 데이터개수 출력::" +  enableCourses.size());

            // 예상 2페이지 결과 데이터개수: 2개
            // 페이징당 최대 20 개의 데이터만 조회가능.
            Page < CourseResponseDto > expectedPageOne = new PageImpl<>(enableCourses, PageRequest.of(page, pageSize), filteredEnableCourses.size());
            when(courseRepository.getEnableEnrollCourses(eq(now), eq(page))).thenReturn(expectedPageOne);

            // when
            Page<CourseResponseDto> resultPageOne = courseService.getEnableCourses(page);

            // then
            assertNotNull(resultPageOne);
            assertEquals(2, resultPageOne.getContent().size()); // 1페이지 결과 데이터개수 확인
            assertEquals("테스트 강좌 26", resultPageOne.getContent().get(0).name()); // 맨첫번째 데이터 강좌명 비교
            assertEquals("강연자 27", resultPageOne.getContent().get(1).lecturerName()); // 두번째 데이터 강연자명 비교
            verify(courseRepository, times(1)).getEnableEnrollCourses(eq(now), eq(page));
        }
        @Test
        @DisplayName("강좌를 신청을 성공한다")
        public void applyCourse() {
            // given
            LocalDate now = LocalDate.now();
            long courseId = 1L;

            // 기존 신청 강좌정보 (신청인원수 0명)
            CourseResponseDto courseDto = new CourseResponseDto(
                    courseId,
                    "테스트 강좌명",
                    "테스트 강연자명",
                    now,  // 수강신청시작날짜: 현재날짜
                    now.plusDays(1), // 수강신청종료날짜: 현재날짜+1일
                    0 // 현재 신청인원수
            );
            long userId = 1L;
            UserResponseDto userDto = new UserResponseDto(userId, "최은강"); // 유저
            Enrollment enrollment = Enrollment
                    .builder()
                    .course(courseDto.toEntity())
                    .user(userDto.toEntity())
                    .build(); // 수강신청

            // 수정된 강좌정보 (신청인원수 1명)
            CourseResponseDto updatedCourseDto = new CourseResponseDto(
                    courseId,
                    "테스트 강좌명",
                    "테스트 강연자명",
                    now,
                    now.plusDays(1),
                    1 // updated: 현재 신청인원수
            );
            when(userRepository.findById(userId)).thenReturn(Optional.of(userDto)); // isAvailableUser 호출확인
            when(courseRepository.findById(courseId)).thenReturn(Optional.of(courseDto)); // isAvailableCourse 호출확인
            when(enrollmentRepository.findByUserIdAndCourseId(userId, courseId)).thenReturn(Optional.empty()); // isAvailableCourse 호출확인
            when(courseRepository.findEnableCourse(courseId, now)).thenReturn(Optional.of(courseDto)); // isAvailableEnrollCourseNow 호출확인
            doNothing().when(enrollmentRepository).create(any(Enrollment.class)); // 정확한 객체비교가 아닌 enrollmentService.create 호출 확인
            doNothing().when(courseRepository).update(any(Course.class)); // 정확한 객체비교가 아닌 courseRepository.update 호출 확인

            // 강좌정보 수정 요청 dto
            // when
            int result = courseService.applyCourse(userId, courseId);

            // then
            assertEquals(updatedCourseDto.currentEnrollments(), result);
            verify(userRepository, times(1)).findById(userId);
            verify(courseRepository, times(2)).findById(courseId); // isAvailableCourse 를 총 2s번 호출
            verify(enrollmentRepository, times(1)).findByUserIdAndCourseId(userId, courseId);
            verify(courseRepository, times(1)).findEnableCourse(courseId, now);
            verify(enrollmentRepository, times(1)).create(any(Enrollment.class));
            verify(courseRepository, times(1)).update(any(Course.class));
        }

        @Test
        @DisplayName("update 테스트")
        public void update_shouldReturn_success() {
            // given
            // 강좌정보 수정 요청 dto
            LocalDate now = LocalDate.now();
            String updatedName = "테스트 강좌명 2";
            String updatedLecturerName = "테스트 강연자명 2";

            long courseId = 1L;
            // 기존 신청 강좌정보 (신청인원수 0명)
            CourseResponseDto courseDto = new CourseResponseDto(
                    courseId,
                    "테스트 강좌명",
                    "테스트 강연자명",
                    now,  // 수강신청시작날짜: 현재날짜
                    now.plusDays(1), // 수강신청종료날짜: 현재날짜+1일
                    0 // 현재 신청인원수
            );
            // 수정요청 dto
            UpdateCourseRequestDto updateRequestDto = new UpdateCourseRequestDto(
                    courseId,
                    updatedName,
                    updatedLecturerName,
                    null,
                    null,
                    1
            );
            // (예상) 수정된 강좌정보 (신청인원수 1명)
            Course expectedUpdatedCourse = new Course(
                    courseId,
                    updatedName, // updated
                    updatedLecturerName, // updated
                    now,
                    now.plusDays(1),
                    1 // updated: 현재 신청인원수
            );
            when(courseRepository.findById(courseId)).thenReturn(Optional.of(courseDto)); // isAvailableCourse 검증됨
            // 객체 일치를 검증하지 않고 호출여부만 확인
            doNothing().when(courseRepository).update(any(Course.class)); // currentEnrollment + 1 로 수정

            // when
            CourseResponseDto result = courseService.updateCourseInfo(updateRequestDto);

            // then
            assertNotNull(result);
            assertEquals(expectedUpdatedCourse.getCurrentEnrollments(), result.currentEnrollments());
            assertEquals(expectedUpdatedCourse.getName(), result.name());
            assertEquals(expectedUpdatedCourse.getLecturerName(), result.lecturerName());
            verify(courseRepository, times(1)).update(any(Course.class));
            verify(courseRepository, times(1)).findById(courseId);
        }
        @Test
        @DisplayName("신청완료한 강의목록 조회를 성공한다.")
        public void getEnrolledFinishedCourses() {
            // given
            long userId = 1L;
            UserResponseDto userDto = new UserResponseDto(userId, "최은강");

            List<EnrolledCourseResponseDto> expectedEnrolledCourses = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                long id = i;
                String courseName = "신청강좌" + i;
                String lecturerName = "강연자" + i;
                LocalDateTime createdAt = LocalDateTime.of(2024, 12, 23, 0, 0);
                EnrolledCourseResponseDto enrolledCourse = new EnrolledCourseResponseDto(id, courseName, lecturerName, createdAt.plusDays(i));
                expectedEnrolledCourses.add(enrolledCourse);
            }

            when(userRepository.findById(userId)).thenReturn(Optional.of(userDto)); // isAvailableCourse 검증됨
            when(enrollmentRepository.getEnrolledCourses(userId)).thenReturn(expectedEnrolledCourses);

            // when
            List<EnrolledCourseResponseDto> result = courseService.getEnrollFinishedCourses(userId);

            // then
            assertEquals(3, result.size());
            verify(userRepository, times(1)).findById(userId);
            verify(enrollmentRepository, times(1)).getEnrolledCourses(userId);

        }
    }

}

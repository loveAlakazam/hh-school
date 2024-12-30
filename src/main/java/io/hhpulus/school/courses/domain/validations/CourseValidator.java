package io.hhpulus.school.courses.domain.validations;

import java.time.LocalDate;

// 유효성 검사
public interface CourseValidator {

    String USER_ID_POSITIVE_NUMBER_VALIDATION_POLICY = "유저 아이디는 0보다 큰 양수여야 합니다.";

    String COURSE_ID_POSITIVE_NUMBER_VALIDATION_POLICY = "강좌 아이디는 0보다 큰 양수여야 합니다.";

    String COURSE_NAME_NOT_EMPTY_VALIDATION_POLICY = "강좌명은 빈공백이거나 null 을 허용하지 않습니다.";

    String LECTURER_NAME_NOT_EMPTY_VALIDATION_POLICY = "강연자명은 빈공백이거나 null 을 허용하지 않습니다.";

    String ENROLL_START_DATE_IS_NOT_NULL = "수강신청 시작날짜는 null 을 허용하지 않습니다.";
    String ENROLL_END_DATE_IS_NOT_NULL = "수강신청 종료날짜는 null 을 허용하지 않습니다.";
    String ENROLL_END_DATE_LATER_THAN_ENROLL_START_DATE = "수강신청 종료날짜는 수강신청 시작날짜보다 앞에 올 수 없습니다.";

    String PAGE_POSITIVE_NUMBER_VALIDATION_POLICY = "페이지는 0보다 큰 양수여야 합니다.";

    String CURRENT_ENROLLMENTS_NUMBER_VALIDATION_POLICY = "현재 수강신청 인원수는 0 보다 큰 양수여야 합니다.";


    // 유효성 검사 로직
    static void checkUserId(long userId) throws IllegalArgumentException {
        // 유저 아이디는 0보다 큰 양수이다.
        if (userId > 0) return;

        throw new IllegalArgumentException(USER_ID_POSITIVE_NUMBER_VALIDATION_POLICY);

    }

    static void checkCourseId(long courseId) throws IllegalArgumentException {
        // 강좌 아이디는 0보다 큰 양수이다.
        if (courseId > 0) return;

        throw new IllegalArgumentException(COURSE_ID_POSITIVE_NUMBER_VALIDATION_POLICY);
    }

    static void checkCourseName(String courseName) throws IllegalArgumentException {
        // 강좌명은 빈공백(화이트스페이스)거나 null 을 허용하지 않는다.
        if (courseName == null || courseName.isBlank()) {
            throw new IllegalArgumentException(COURSE_NAME_NOT_EMPTY_VALIDATION_POLICY);
        }
    }

    static void checkLecturerName(String lecturerName) throws IllegalArgumentException {
        // 강연자명은 빈공백(화이트스페이스)거나 null 을 허용하지 않는다.
        if (lecturerName == null || lecturerName.isBlank()) {
            throw new IllegalArgumentException(LECTURER_NAME_NOT_EMPTY_VALIDATION_POLICY);
        }
    }

    static void checkEnrollStartDate(LocalDate enrollStartDate) throws IllegalArgumentException {
        // 수강신청시작날짜는 null 을 허용하지 않는다.
        if (enrollStartDate == null) {
            throw new IllegalArgumentException(ENROLL_START_DATE_IS_NOT_NULL);
        }
    }
    static void checkEnrollEndDate(LocalDate enrollStartDate, LocalDate enrollEndDate) throws IllegalArgumentException {
        // 수강신청 종료 날짜는 null 을 허용하지 않는다.
        if (enrollEndDate == null) {
            throw new IllegalArgumentException(ENROLL_END_DATE_IS_NOT_NULL);
        }

        // 수강신청 종료날짜는 수강신청시작날짜보다 미래이다.
        if (enrollEndDate.compareTo(enrollStartDate) > 0) return;
        throw new IllegalArgumentException(ENROLL_END_DATE_LATER_THAN_ENROLL_START_DATE);
    }

    static void checkPage(int page) throws IllegalArgumentException {
        // 페이지네이션 페이지는 0보다 큰 양수이다.
        if (page > 0) return;

        throw new IllegalArgumentException(PAGE_POSITIVE_NUMBER_VALIDATION_POLICY);
    }

    static void checkCurrentEnrollments (int currentEnrollments) throws IllegalArgumentException {
        // 현재 강의신청 인원수는 0보다 큰 양수이다.
        if(currentEnrollments > 0) return;
        throw new IllegalArgumentException(CURRENT_ENROLLMENTS_NUMBER_VALIDATION_POLICY);
    }
}

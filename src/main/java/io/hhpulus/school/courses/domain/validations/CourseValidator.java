package io.hhpulus.school.courses.domain.validations;

import java.time.LocalDate;

// 유효성 검사
public interface CourseValidator {
    int ENROLLMENT_MAXIMUM_DAYS = 364;

    String USER_ID_POSITIVE_NUMBER_VALIDATION_POLICY = "유저 아이디는 0보다 큰 양수여야 합니다.";

    String COURSE_ID_POSITIVE_NUMBER_VALIDATION_POLICY = "강좌 아이디는 0보다 큰 양수여야 합니다.";

    String DAYS_POSITIVE_NUMBER_VALIDATION_POLICY = "수강신청 기간(일)은 0보다 큰 양수여야 합니다.";

    String COURSE_NAME_NOT_EMPTY_VALIDATION_POLICY = "강좌명은 빈공백이거나 null 을 허용하지 않습니다.";

    String LECTURER_NAME_NOT_EMPTY_VALIDATION_POLICY = "강연자명은 빈공백이거나 null 을 허용하지 않습니다.";

    String ENROLLMENT_DAYS_MAXIMUM_VALIDATION_POLICY = "수강신청 기간(일)은 최대 " + ENROLLMENT_MAXIMUM_DAYS + "일 입니다.";

    String ENROLL_START_DATE_IS_NOT_NULL = "수강 신청 시작기간은 null 을 허용하지 않습니다.";

    String PAGE_POSITIVE_NUMBER_VALIDATION_POLICY = "페이지는 0보다 큰 양수여야 합니다.";


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

    static void checkDays(int days) throws IllegalArgumentException{
        // 수강신청기간(일)은 1~364일 까지 허용한다.
        if (days <= 0)
            throw new IllegalArgumentException(DAYS_POSITIVE_NUMBER_VALIDATION_POLICY);

        if (days > ENROLLMENT_MAXIMUM_DAYS)
            throw new IllegalArgumentException(ENROLLMENT_DAYS_MAXIMUM_VALIDATION_POLICY);
    }

    static void checkEnrollStartDate(LocalDate enrollStartDate) throws IllegalArgumentException {

        // 수강신청시작날짜는 null 을 허용하지 않는다.
        if (enrollStartDate == null) {
            throw new IllegalArgumentException(ENROLL_START_DATE_IS_NOT_NULL);
        }
    }

    static void checkPage(int page) throws IllegalArgumentException {
        // 페이지네이션 페이지는 0보다 큰 양수이다.
        if (page > 0) return;

        throw new IllegalArgumentException(PAGE_POSITIVE_NUMBER_VALIDATION_POLICY);
    }
}

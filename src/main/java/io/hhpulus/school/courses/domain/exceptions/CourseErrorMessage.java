package io.hhpulus.school.courses.domain.exceptions;

public interface CourseErrorMessage {
    // errorHandler messages
    String COURSE_ENROLL_DISABLE = "신청할 수 없는 강좌 입니다.";
    String COURSE_NOT_FOUND = "존재하지 않은 강좌 입니다.";

    // exception messages
    String COURSE_ID_DOES_NOT_EXISTS = "존재하지 않은 강좌아이디 입니다.";
    String COURSE_ALREADY_ENROLLED = "이미 신청 완료된 강좌 입니다.";
    String COURSE_ENROLL_DATE_EXPIRED = "현재 신청할 수 없는 강좌 입니다.";
    String COURSE_ENROLL_DISABLED = "수강신청이 종료된 강좌 입니다.";
}

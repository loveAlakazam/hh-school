package io.hhpulus.school.courses.domain.exceptions;

public interface CourseErrorMessage {
    String COURSE_ENROLL_DISABLE = "신청할 수 없는 강좌입니다.";
    String COURSE_ID_DOES_NOT_EXISTS = "존재하지 않은 강좌아이디 입니다.";
    String COURSE_ALREADY_ENROLLED = "이미 신청완료된 강좌입니다.";
}

package io.hhpulus.school.enrollments.domain;


public interface EnrollmentService {
    void addEnrollment(); // 수강신청등록
    void getEnrollmentsByUserId(long userId); // 유저별 수강신청 완료 목록 조회 - 특정 userId로 신청완료된 특강목록 조회
    void getEnrollmentsByCourseId(long courseId); // 강좌별 신청목록 조회 - 특정 courseId 로 신청자의 정보를 조회
}

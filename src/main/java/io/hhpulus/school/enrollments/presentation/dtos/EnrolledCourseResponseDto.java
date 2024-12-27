package io.hhpulus.school.enrollments.presentation.dtos;

import java.time.LocalDate;

// 신청완료된 특강
public class EnrolledCourseResponseDto {
    long courseId; // 강의 아이디
    long courseName; // 강의명
    long lecturerName; // 강연자 이름
    LocalDate createdAt; // 수강신청날짜

    // generators
    public EnrolledCourseResponseDto() {}

    public EnrolledCourseResponseDto(long courseId, long courseName, long lecturerName, LocalDate createdAt) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.lecturerName = lecturerName;
        this.createdAt = createdAt;
    }

    // getter & setter
    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getCourseName() {
        return courseName;
    }

    public void setCourseName(long courseName) {
        this.courseName = courseName;
    }

    public long getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(long lecturerName) {
        this.lecturerName = lecturerName;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}

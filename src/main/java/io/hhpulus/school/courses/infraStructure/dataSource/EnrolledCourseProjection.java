package io.hhpulus.school.courses.infraStructure.dataSource;

import java.time.LocalDateTime;

public interface EnrolledCourseProjection {
    long getCourseId();
    String getCourseName();
    String getLecturerName();
    LocalDateTime getCreatedAt();
}

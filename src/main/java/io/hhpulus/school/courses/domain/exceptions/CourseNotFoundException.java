package io.hhpulus.school.courses.domain.exceptions;

import org.springframework.http.ResponseEntity;

public class CourseNotFoundException extends RuntimeException {
    private String message;
    public CourseNotFoundException(String message) {
        super(message);}
}

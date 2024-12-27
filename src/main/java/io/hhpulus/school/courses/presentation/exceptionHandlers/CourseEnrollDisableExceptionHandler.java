package io.hhpulus.school.courses.presentation.exceptionHandlers;

import io.hhpulus.school.commons.presentation.dtos.ErrorResponseDto;
import io.hhpulus.school.courses.domain.exceptions.CourseEnrollDisableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static io.hhpulus.school.courses.domain.exceptions.CourseErrorMessage.COURSE_ENROLL_DISABLE;

@RestControllerAdvice
public class CourseEnrollDisableExceptionHandler {
    @ExceptionHandler(CourseEnrollDisableException.class)
    public ResponseEntity<ErrorResponseDto> handleCourseEnrollDisableException(CourseEnrollDisableException exception) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(COURSE_ENROLL_DISABLE, exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}

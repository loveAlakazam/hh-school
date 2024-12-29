package io.hhpulus.school.courses.presentation.exceptionHandlers;

import io.hhpulus.school.commons.presentation.dtos.ErrorResponseDto;
import io.hhpulus.school.courses.domain.exceptions.CourseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static io.hhpulus.school.courses.domain.exceptions.CourseErrorMessage.COURSE_NOT_FOUND;

@RestControllerAdvice
public class CourseNotFoundExceptionHandler {
    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleCourseNotFoundException(CourseNotFoundException exception) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(COURSE_NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }
}

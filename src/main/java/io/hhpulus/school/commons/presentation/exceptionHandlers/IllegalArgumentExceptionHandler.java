package io.hhpulus.school.commons.presentation.exceptionHandlers;

import io.hhpulus.school.commons.presentation.dtos.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static io.hhpulus.school.commons.domain.exceptions.GlobalErrorMessages.BAD_REQUEST_ERROR_MESSAGE;

@RestControllerAdvice
public class IllegalArgumentExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handlerIllegalArgumentException(Exception exception) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(BAD_REQUEST_ERROR_MESSAGE, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}

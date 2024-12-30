package io.hhpulus.school.commons.presentation.exceptionHandlers;

import io.hhpulus.school.commons.presentation.dtos.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static io.hhpulus.school.commons.domain.exceptions.GlobalErrorMessages.INTERNAL_SERVER_ERROR_MESSAGE;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handlerGlobalException(Exception exception) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(INTERNAL_SERVER_ERROR_MESSAGE, exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

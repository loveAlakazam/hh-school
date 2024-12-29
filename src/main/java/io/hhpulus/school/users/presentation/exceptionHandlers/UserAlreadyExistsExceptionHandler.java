package io.hhpulus.school.users.presentation.exceptionHandlers;

import io.hhpulus.school.commons.presentation.dtos.ErrorResponseDto;
import io.hhpulus.school.users.domain.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserAlreadyExistsExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAlreadyExistsException (UserAlreadyExistsException exception) {
        String title = "해당 유저는 이미 존재합니다.";
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                title,
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

}

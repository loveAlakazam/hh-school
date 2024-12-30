package io.hhpulus.school.users.domain.exceptions;

public class UserAlreadyExistsException extends  RuntimeException {
    private String message;
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

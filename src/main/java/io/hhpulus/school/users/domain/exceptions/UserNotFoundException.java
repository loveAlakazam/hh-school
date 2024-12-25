package io.hhpulus.school.users.domain.exceptions;


public class UserNotFoundException extends RuntimeException {
    private String message;
    public UserNotFoundException(String message) {
        super(message);
    }
}

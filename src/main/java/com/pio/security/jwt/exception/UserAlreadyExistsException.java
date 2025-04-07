package com.pio.security.jwt.exception;

public class UserAlreadyExistsException extends RuntimeException {
    private String message;
    private int httpStatus;

    public UserAlreadyExistsException(int httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}

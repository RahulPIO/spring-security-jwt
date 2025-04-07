package com.pio.security.jwt.exception;

public class UserNotFoundException extends RuntimeException {
    private int httpStatus;
    private String message;

    public UserNotFoundException(int httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

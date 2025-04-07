package com.pio.security.jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.UndeclaredThrowableException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> noSuchElementException(UserNotFoundException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(Exception e) {
        if (e instanceof UndeclaredThrowableException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User doesn't exist");
        }
        return ResponseEntity.status(500).body(e.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> invalidCredentialsException(InvalidCredentialsException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> userAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}

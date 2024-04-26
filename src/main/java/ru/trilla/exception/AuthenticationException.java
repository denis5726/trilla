package ru.trilla.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends RestApiException {

    public AuthenticationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}

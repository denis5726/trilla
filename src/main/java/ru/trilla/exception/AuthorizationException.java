package ru.trilla.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends RestApiException {

    public AuthorizationException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}

package ru.trilla.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends RestApiException {

    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

package ru.trilla.exception;

import org.springframework.http.HttpStatus;

public class DataValidationException extends RestApiException {

    public DataValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

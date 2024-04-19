package ru.trilla.exception;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends RestApiException {

    public ResourceAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}

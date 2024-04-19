package ru.trilla.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestApiException extends TrillaException {
    private final HttpStatus status;

    public RestApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}

package ru.trilla.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponseDto(
    HttpStatus status,
    String message,
    LocalDateTime time,
    String path
) {
}

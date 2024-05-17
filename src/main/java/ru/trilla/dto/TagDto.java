package ru.trilla.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TagDto(
        UUID id,
        String value,
        LocalDateTime createdAt
) {
}

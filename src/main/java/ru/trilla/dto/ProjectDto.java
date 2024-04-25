package ru.trilla.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectDto(
        UUID id,
        String fullName,
        String code,
        LocalDateTime createdAt
) {
}

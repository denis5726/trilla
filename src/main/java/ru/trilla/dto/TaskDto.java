package ru.trilla.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskDto(
        UUID id,
        String code,
        String summary,
        String description,
        String assigneeFirstName,
        String assigneeLastName,
        String status,
        String type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

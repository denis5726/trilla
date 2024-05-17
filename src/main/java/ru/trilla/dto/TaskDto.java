package ru.trilla.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskDto(
        UUID id,
        String code,
        String summary,
        String description,
        RelatedUserDto assigneeInfo,
        String status,
        String type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

package ru.trilla.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AttachmentDto(
        UUID id,
        String name,
        String url,
        LocalDateTime createdAt
) {
}

package ru.trilla.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentDto(
        UUID id,
        String text,
        RelatedUserDto author,
        LocalDateTime createdAt
) {
}

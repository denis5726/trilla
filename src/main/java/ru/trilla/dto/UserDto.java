package ru.trilla.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDto(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String passwordHash,
        LocalDateTime createdAt
) {
}

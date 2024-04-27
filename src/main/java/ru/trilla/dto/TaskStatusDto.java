package ru.trilla.dto;

import java.util.UUID;

public record TaskStatusDto(
        UUID id,
        String name
) {
}

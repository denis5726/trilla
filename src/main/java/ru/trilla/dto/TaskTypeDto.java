package ru.trilla.dto;

import java.util.UUID;

public record TaskTypeDto(
        UUID id,
        String name,
        UUID projectId
) {
}

package ru.trilla.dto;

import java.util.UUID;

public record DetailedLinkTypeDto(
        UUID id,
        String name,
        UUID taskTypeFromId,
        UUID taskTypeInId
) {
}

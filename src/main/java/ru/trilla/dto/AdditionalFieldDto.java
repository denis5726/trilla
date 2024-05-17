package ru.trilla.dto;

import java.util.UUID;

public record AdditionalFieldDto(
        UUID id,
        AdditionalFieldTypeDto type,
        String value
) {
}

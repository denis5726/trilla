package ru.trilla.dto;

import ru.trilla.entity.AdditionalFieldValueType;

import java.util.UUID;

public record AdditionalFieldTypeDto(
        UUID id,
        String name,
        AdditionalFieldValueType valueType
) {
}

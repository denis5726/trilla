package ru.trilla.dto;

import java.util.UUID;

public record RelatedUserDto(
        UUID id,
        String firstName,
        String lastName
) {
}

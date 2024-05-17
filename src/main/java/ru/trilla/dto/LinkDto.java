package ru.trilla.dto;

import java.util.UUID;

public record LinkDto(
        LinkTypeDto type,
        UUID to
) {
}

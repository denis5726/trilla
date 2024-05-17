package ru.trilla.dto;

import ru.trilla.entity.LinkType;

import java.util.UUID;

public record LinkDto(
        LinkTypeDto type,
        UUID to
) {
}

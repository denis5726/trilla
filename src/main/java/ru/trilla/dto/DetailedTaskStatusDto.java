package ru.trilla.dto;

import java.util.UUID;

public record DetailedTaskStatusDto(
        UUID id,
        String name,
        Boolean isOpened,
        Boolean isInitial
) {
}

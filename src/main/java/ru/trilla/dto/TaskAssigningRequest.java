package ru.trilla.dto;

import java.util.UUID;

public record TaskAssigningRequest(
        UUID taskId,
        UUID userId
) {
}

package ru.trilla.dto;

import java.util.UUID;

public record TaskStatusUpdatingRequest(
        UUID taskId,
        UUID newTaskStatusId
) {
}

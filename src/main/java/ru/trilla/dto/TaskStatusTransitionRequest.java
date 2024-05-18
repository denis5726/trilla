package ru.trilla.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TaskStatusTransitionRequest(
        @NotNull(message = "Поле обязательно к заполнению")
        UUID taskId,
        @NotNull(message = "Поле обязательно к заполнению")
        UUID newTaskStatusId
) {
}

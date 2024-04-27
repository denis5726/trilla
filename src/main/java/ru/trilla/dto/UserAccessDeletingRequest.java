package ru.trilla.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserAccessDeletingRequest(
        @NotNull(message = "Идентификатор пользователя обязателен к заполнению")
        UUID userId,
        @NotNull(message = "Идентификатор проекта обязателен к заполнению")
        UUID projectId
) {
}

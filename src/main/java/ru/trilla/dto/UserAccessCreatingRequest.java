package ru.trilla.dto;

import jakarta.validation.constraints.NotNull;
import ru.trilla.entity.Role;

import java.util.UUID;

public record UserAccessCreatingRequest(
        @NotNull(message = "Идентификатор пользователя обязателен к заполнению")
        UUID userId,
        @NotNull(message = "Идентификатор проекта обязателен к заполнению")
        UUID projectId,
        @NotNull(message = "Роль обязательна к заполнению")
        Role role
) {
}

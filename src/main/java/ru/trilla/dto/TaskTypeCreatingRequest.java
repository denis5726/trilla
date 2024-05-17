package ru.trilla.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record TaskTypeCreatingRequest(
        @NotNull(message = "Поле обязательно к заполнению")
        @Length(min = 3, max = 255, message = "Длина поля должна быть от 3 до 255 символов")
        String name,
        @NotNull(message = "Поле обязательно к заполнению")
        UUID projectId
) {
}

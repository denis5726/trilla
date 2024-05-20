package ru.trilla.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record LinkTypeCreatingRequest(
        @NotNull(message = "Поле обязательно к заполнению")
        UUID taskTypeFromId,
        @NotNull(message = "Поле обязательно к заполнению")
        UUID taskTypeInId,
        @NotNull(message = "Поле обязательно к заполнению")
        @Length(min = 3, max = 255, message = "Длина поля должна быть от 3 до 255 символов")
        String name
) {
}

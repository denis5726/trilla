package ru.trilla.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record LinkTypeUpdatingRequest(
        @NotNull(message = "Поле обязательно к заполнению")
        UUID id,
        @NotNull(message = "Поле обязательно к заполнению")
        @Length(min = 3, max = 255, message = "Длина поля должна быть от 3 до 255 символов")
        String name
) {
}

package ru.trilla.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProjectCreatingRequest(
        @NotNull(message = "Название проекта обязательно к заполнению")
        @Size(min = 3, max = 255, message = "Длина названия проекта должна быть между 3 и 255 символами")
        String fullName,
        @NotNull(message = "Код проекта обязателен к заполнению")
        @Size(min = 3, max = 10, message = "Длина кода проекта должна быть между 3 и 10 символами")
        @Pattern(regexp = "[A-Z]*", message = "Код проекта должен состоять только из заглавных латинских букв")
        String code
) {
}

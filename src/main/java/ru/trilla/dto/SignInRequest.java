package ru.trilla.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignInRequest(
        @NotNull(message = "Email обязателен для регистрации")
        @Email(message = "Email не валидный")
        String email,
        @NotNull(message = "Пароль обязателен к заполнению")
        String password
) {
}

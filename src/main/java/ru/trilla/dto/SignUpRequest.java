package ru.trilla.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record SignUpRequest(
        @NotNull(message = "Email обязателен для регистрации")
        @Email(message = "Email не валидный")
        String email,
        @NotNull(message = "Имя обязательно для заполнения")
        @Length(min = 3, max = 255, message = "Длина имени должна быть между 3 и 255 символами")
        String firstName,
        @NotNull(message = "Фамилия обязательна для заполнения")
        @Length(min = 3, max = 255, message = "Длина фамилии должна быть между 3 и 255 символами")
        String lastName,
        @NotNull(message = "Пароль обязателен к заполнению")
        String password
) {
}

package ru.trilla.dto;

import ru.trilla.entity.Role;

import java.util.UUID;

public record UserAccessDto(
        UUID userId,
        String email,
        String firstName,
        String lastName,
        UUID projectId,
        String projectCode,
        String projectFullName,
        Role role
) {
}

package ru.trilla.service;

import ru.trilla.entity.Project;
import ru.trilla.entity.Task;

import java.util.UUID;

public interface UserAccessAuthorizer {

    void checkAccess(UUID userId, Project project);

    void checkAccess(UUID userId, Project project, String message);

    void checkAccess(UUID userId, Task task);
}

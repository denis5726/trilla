package ru.trilla.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.trilla.entity.Project;
import ru.trilla.entity.Task;
import ru.trilla.exception.AuthorizationException;
import ru.trilla.repository.UserAccessRepository;
import ru.trilla.service.UserAccessAuthorizer;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserAccessAuthorizerImpl implements UserAccessAuthorizer {
    private final UserAccessRepository repository;

    @Override
    public void checkAccess(UUID userId, Project project) {
        checkAccess(userId, project, "Вы не имеете доступа к данному проекту");
    }

    @Override
    public void checkAccess(UUID userId, Project project, String message) {
        if (!repository.existsByIdUserIdAndIdProjectId(userId, project.getId())) {
            throw new AuthorizationException(message);
        }
    }

    @Override
    public void checkAccess(UUID userId, Task task) {
        if (!repository.existsByIdUserIdAndIdProjectId(userId, task.getProject().getId())) {
            throw new AuthorizationException("Вы не имеете доступа к данной задаче");
        }
    }
}

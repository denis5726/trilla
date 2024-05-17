package ru.trilla.service;

import ru.trilla.dto.TaskTypeCreatingRequest;
import ru.trilla.dto.TaskTypeDto;
import ru.trilla.dto.TaskTypeUpdatingRequest;
import ru.trilla.security.TrillaAuthentication;

import java.util.List;
import java.util.UUID;

public interface TaskTypeService {

    List<TaskTypeDto> findByProject(UUID projectId, TrillaAuthentication authentication);

    TaskTypeDto create(TaskTypeCreatingRequest request, TrillaAuthentication authentication);

    TaskTypeDto updateName(TaskTypeUpdatingRequest request, TrillaAuthentication authentication);

    void deleteById(UUID taskTypeId, TrillaAuthentication authentication);
}

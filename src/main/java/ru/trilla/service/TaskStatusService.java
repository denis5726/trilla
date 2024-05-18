package ru.trilla.service;

import ru.trilla.dto.DetailedTaskStatusDto;
import ru.trilla.dto.TaskStatusCreatingRequest;
import ru.trilla.dto.TaskStatusUpdatingRequest;
import ru.trilla.security.TrillaAuthentication;

import java.util.List;
import java.util.UUID;

public interface TaskStatusService {

    List<DetailedTaskStatusDto> findByTaskType(UUID taskTypeId, TrillaAuthentication authentication);

    DetailedTaskStatusDto create(TaskStatusCreatingRequest request, TrillaAuthentication authentication);

    DetailedTaskStatusDto update(TaskStatusUpdatingRequest request, TrillaAuthentication authentication);

    void delete(UUID taskStatusId, TrillaAuthentication authentication);
}

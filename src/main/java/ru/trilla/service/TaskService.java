package ru.trilla.service;

import ru.trilla.dto.TaskAssigningRequest;
import ru.trilla.dto.TaskCreatingRequest;
import ru.trilla.dto.TaskDto;
import ru.trilla.dto.TaskStatusDto;
import ru.trilla.dto.TaskStatusTransitionRequest;
import ru.trilla.security.TrillaAuthentication;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    List<TaskDto> findActualAndAssigneeOnMe(TrillaAuthentication authentication);

    List<TaskStatusDto> findPossibleStatusesAfterTransition(UUID taskId, TrillaAuthentication authentication);

    TaskDto create(TaskCreatingRequest request, TrillaAuthentication authentication);

    TaskDto assigneeUser(TaskAssigningRequest request, TrillaAuthentication authentication);

    TaskDto updateStatus(TaskStatusTransitionRequest request, TrillaAuthentication authentication);

    TaskDto update(UUID taskId, TaskCreatingRequest request, TrillaAuthentication authentication);

    void deleteById(UUID taskId, TrillaAuthentication authentication);
}

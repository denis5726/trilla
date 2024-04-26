package ru.trilla.service;

import ru.trilla.dto.TaskCreatingRequest;
import ru.trilla.dto.TaskDto;
import ru.trilla.security.TrillaAuthentication;

import java.util.List;

public interface TaskService {

    List<TaskDto> findActualAndAssigneeOnMe(TrillaAuthentication authentication);

    TaskDto create(TaskCreatingRequest request, TrillaAuthentication authentication);
}

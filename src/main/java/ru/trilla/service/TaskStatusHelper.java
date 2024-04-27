package ru.trilla.service;

import ru.trilla.entity.TaskStatus;
import ru.trilla.entity.TaskType;

import java.util.List;

public interface TaskStatusHelper {

    List<TaskStatus> findPossibleTransitions(TaskStatus taskStatus);

    TaskStatus resolveInitialStatusForTaskType(TaskType taskType);
}

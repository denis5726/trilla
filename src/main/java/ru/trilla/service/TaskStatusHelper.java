package ru.trilla.service;

import ru.trilla.entity.TaskStatus;

import java.util.List;

public interface TaskStatusHelper {

    List<TaskStatus> findPossibleTransitions(TaskStatus taskStatus);
}

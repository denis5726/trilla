package ru.trilla.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.trilla.entity.TaskStatus;
import ru.trilla.entity.TaskStatusTransition;
import ru.trilla.entity.TaskType;
import ru.trilla.repository.TaskStatusRepository;
import ru.trilla.repository.TaskStatusTransitionRepository;
import ru.trilla.service.TaskStatusHelper;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class TaskStatusHelperImpl implements TaskStatusHelper {
    private final TaskStatusRepository repository;
    private final TaskStatusTransitionRepository taskStatusTransitionRepository;

    @Override
    public List<TaskStatus> findPossibleTransitions(TaskStatus taskStatus) {
        return taskStatusTransitionRepository.findByIdFrom(taskStatus).stream()
                .map(TaskStatusTransition::getId)
                .map(TaskStatusTransition.Id::getIn)
                .toList();
    }

    @Override
    public TaskStatus resolveInitialStatusForTaskType(TaskType taskType) {
        return repository.findByTaskTypeAndIsInitial(taskType, true).orElseThrow();
    }
}

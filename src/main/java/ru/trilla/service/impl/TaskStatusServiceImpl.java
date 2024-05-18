package ru.trilla.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trilla.dto.DetailedTaskStatusDto;
import ru.trilla.dto.TaskStatusCreatingRequest;
import ru.trilla.dto.TaskStatusUpdatingRequest;
import ru.trilla.entity.Role;
import ru.trilla.entity.TaskStatus;
import ru.trilla.entity.TaskType;
import ru.trilla.exception.DataValidationException;
import ru.trilla.mapper.TaskStatusMapper;
import ru.trilla.repository.TaskRepository;
import ru.trilla.repository.TaskStatusRepository;
import ru.trilla.repository.TaskTypeRepository;
import ru.trilla.security.TrillaAuthentication;
import ru.trilla.service.TaskStatusService;
import ru.trilla.service.UserAccessAuthorizer;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {
    private final TaskStatusRepository repository;
    private final UserAccessAuthorizer authorizer;
    private final TaskStatusMapper mapper;
    private final TaskTypeRepository taskTypeRepository;
    private final TaskRepository taskRepository;

    @Override
    public List<DetailedTaskStatusDto> findByTaskType(UUID taskTypeId, TrillaAuthentication authentication) {
        final var taskType = taskTypeRepository.findById(taskTypeId).orElseThrow();
        authorizer.checkAccess(authentication.id(), taskType.getProject());
        return mapper.toDtoList(repository.findByTaskType(taskType));
    }

    @Override
    @Transactional
    public DetailedTaskStatusDto create(TaskStatusCreatingRequest request, TrillaAuthentication authentication) {
        final var taskType = taskTypeRepository.findById(request.taskTypeId()).orElseThrow();
        authorizer.checkAccess(authentication.id(), taskType.getProject(), Role.ADMIN);
        final var taskStatus = mapper.toEntity(request);
        taskStatus.setTaskType(taskType);
        checkStatusMutation(taskStatus, taskType);
        return mapper.toDto(repository.save(taskStatus));
    }

    @Override
    @Transactional
    public DetailedTaskStatusDto update(TaskStatusUpdatingRequest request, TrillaAuthentication authentication) {
        final var taskStatus = repository.findById(request.id()).orElseThrow();
        final var taskType = taskStatus.getTaskType();
        authorizer.checkAccess(authentication.id(), taskType.getProject(), Role.ADMIN);
        mapper.update(taskStatus, request);
        checkStatusMutation(taskStatus, taskType);
        return mapper.toDto(repository.save(taskStatus));
    }

    @Override
    @Transactional
    public void delete(UUID taskStatusId, TrillaAuthentication authentication) {
        final var taskStatus = repository.findById(taskStatusId).orElseThrow();
        authorizer.checkAccess(authentication.id(), taskStatus.getTaskType().getProject(), Role.ADMIN);
        if (taskRepository.existsByTaskStatus(taskStatus)) {
            log.info("Attempt to delete task status (id={}) with existent tasks", taskStatusId);
            throw new DataValidationException("Невозможно удалить статус задачи, потому что есть задачи в этом статусе");
        }
        repository.delete(taskStatus);
    }

    private void checkStatusMutation(TaskStatus updatedEntity, TaskType taskType) {
        if (Boolean.TRUE.equals(updatedEntity.getIsInitial()) && repository.existsByTaskTypeAndIsInitial(taskType, true)) {
            log.info("Attempt to create another initial status in taskType: {}", taskType);
            throw new DataValidationException("В типе задачи уже есть инициирующий статус");
        }
        if (repository.existsByTaskTypeAndName(taskType, updatedEntity.getName())) {
            log.info("Attempt to create task status with existent name={} in taskType: {}", updatedEntity.getName(), taskType);
            throw new DataValidationException(String.format(
                    "Статус задачи с таким именем существует для типа задачи %s",
                    taskType.getName()
            ));
        }
    }
}

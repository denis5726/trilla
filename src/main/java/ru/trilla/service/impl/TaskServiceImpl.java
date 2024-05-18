package ru.trilla.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trilla.dto.TaskAssigningRequest;
import ru.trilla.dto.TaskCreatingRequest;
import ru.trilla.dto.TaskDto;
import ru.trilla.dto.TaskStatusDto;
import ru.trilla.dto.TaskStatusTransitionRequest;
import ru.trilla.entity.Project;
import ru.trilla.entity.Role;
import ru.trilla.entity.Task;
import ru.trilla.exception.DataValidationException;
import ru.trilla.exception.TrillaException;
import ru.trilla.mapper.TaskMapper;
import ru.trilla.repository.TaskRepository;
import ru.trilla.repository.TaskStatusRepository;
import ru.trilla.repository.TaskTypeRepository;
import ru.trilla.repository.UserRepository;
import ru.trilla.security.TrillaAuthentication;
import ru.trilla.service.TaskService;
import ru.trilla.service.TaskStatusHelper;
import ru.trilla.service.UserAccessAuthorizer;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final TaskTypeRepository taskTypeRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusHelper taskStatusHelper;
    private final UserAccessAuthorizer userAccessAuthorizer;
    private final UserRepository userRepository;

    @Override
    public List<TaskDto> findActualAndAssigneeOnMe(TrillaAuthentication authentication) {
        return mapper.toDtoList(repository.findByAssigneeIdAndTaskStatusIsOpened(authentication.id(), true));
    }

    @Override
    public List<TaskStatusDto> findPossibleStatusesAfterTransition(UUID taskId, TrillaAuthentication authentication) {
        final var task = repository.findById(taskId).orElseThrow();
        userAccessAuthorizer.checkAccess(authentication.id(), task);
        return taskStatusHelper.findPossibleTransitions(
                        task.getTaskStatus()
                ).stream()
                .map(taskStatus -> new TaskStatusDto(taskStatus.getId(), taskStatus.getName()))
                .toList();
    }

    @Override
    @Transactional
    public TaskDto create(TaskCreatingRequest request, TrillaAuthentication authentication) {
        final var taskType = taskTypeRepository.findById(request.taskTypeId()).orElseThrow();

        userAccessAuthorizer.checkAccess(authentication.id(), taskType.getProject());

        return mapper.toDto(repository.save(
                Task.builder()
                        .creator(userRepository.findById(authentication.id()).orElseThrow())
                        .project(taskType.getProject())
                        .taskType(taskType)
                        .taskStatus(taskStatusHelper.resolveInitialStatusForTaskType(taskType))
                        .code(generateCode(taskType.getProject()))
                        .summary(request.summary())
                        .description(request.description())
                        .build()
        ));
    }

    @Override
    @Transactional
    public TaskDto assigneeUser(TaskAssigningRequest request, TrillaAuthentication authentication) {
        final var task = repository.findById(request.taskId()).orElseThrow();
        final var project = task.getProject();
        task.setAssignee(userRepository.findById(request.userId()).orElseThrow());
        userAccessAuthorizer.checkAccess(authentication.id(), task);
        userAccessAuthorizer.checkAccess(request.userId(), project, "Пользователь не имеет доступа к проекту");

        return mapper.toDto(repository.save(task));
    }

    @Override
    @Transactional
    public TaskDto updateStatus(TaskStatusTransitionRequest request, TrillaAuthentication authentication) {
        final var task = repository.findById(request.taskId()).orElseThrow();
        userAccessAuthorizer.checkAccess(authentication.id(), task);
        final var newTaskStatus = taskStatusRepository.findById(request.newTaskStatusId()).orElseThrow();
        if (
                taskStatusHelper.findPossibleTransitions(task.getTaskStatus()).stream()
                        .noneMatch(taskStatus -> Objects.equals(newTaskStatus.getId(), taskStatus.getId()))
        ) {
            log.info("Attempt to move task to impossible status");
            throw new DataValidationException("Переход в данный статус невозможен при текущем статусе задачи");
        }
        task.setTaskStatus(newTaskStatus);
        return mapper.toDto(repository.save(task));
    }

    @Override
    public TaskDto update(UUID taskId, TaskCreatingRequest request, TrillaAuthentication authentication) {
        final var task = repository.findById(taskId).orElseThrow();
        userAccessAuthorizer.checkAccess(authentication.id(), task);
        if (!Objects.equals(task.getTaskType().getId(), request.taskTypeId())) {
            final var taskType = taskTypeRepository.findById(request.taskTypeId()).orElseThrow();
            task.setTaskType(taskType);
            task.setTaskStatus(taskStatusHelper.resolveInitialStatusForTaskType(taskType));
        }
        task.setSummary(request.summary());
        task.setDescription(request.description());
        return mapper.toDto(repository.save(task));
    }

    @Override
    public void deleteById(UUID taskId, TrillaAuthentication authentication) {
        final var task = repository.findById(taskId).orElseThrow();
        userAccessAuthorizer.checkAccess(authentication.id(), task.getProject(), Role.ADMIN);
        repository.delete(task);
    }

    private String generateCode(Project project) {
        final var optionalTask = repository.findFirstByProjectOrderByCodeDesc(project);
        if (optionalTask.isEmpty()) {
            return project.getCode() + "-1";
        }
        final var lastCode = optionalTask.get().getCode();
        try {
            final var number = Long.parseLong(lastCode.split("-")[1]);
            return project.getCode() + "-" + (number + 1);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new TrillaException("Внутренняя ошибка системы", e);
        }
    }
}

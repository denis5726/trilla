package ru.trilla.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trilla.dto.TaskAssigningRequest;
import ru.trilla.dto.TaskCreatingRequest;
import ru.trilla.dto.TaskDto;
import ru.trilla.entity.Project;
import ru.trilla.entity.Task;
import ru.trilla.entity.TaskStatus;
import ru.trilla.entity.TaskType;
import ru.trilla.exception.AuthorizationException;
import ru.trilla.exception.DataValidationException;
import ru.trilla.exception.TrillaException;
import ru.trilla.mapper.TaskMapper;
import ru.trilla.repository.TaskRepository;
import ru.trilla.repository.TaskStatusRepository;
import ru.trilla.repository.TaskTypeRepository;
import ru.trilla.repository.UserAccessRepository;
import ru.trilla.security.TrillaAuthentication;
import ru.trilla.service.TaskService;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final TaskTypeRepository taskTypeRepository;
    private final UserAccessRepository userAccessRepository;
    private final TaskStatusRepository taskStatusRepository;

    @Override
    public List<TaskDto> findActualAndAssigneeOnMe(TrillaAuthentication authentication) {
        return mapper.toDtoList(repository.findByAssigneeIdAndTaskStatusIsOpened(authentication.id(), true));
    }

    @Override
    @Transactional
    public TaskDto create(TaskCreatingRequest request, TrillaAuthentication authentication) {
        final var taskType = taskTypeRepository.findById(request.taskTypeId()).orElseThrow(() ->
                new DataValidationException("Тип задачи с заданным идентификатором не найден")
        );
        final var userAccesses = userAccessRepository.findByIdUserId(authentication.id());
        if (userAccesses.stream().noneMatch(userAccess ->
                Objects.equals(
                        userAccess.getId().getProject().getId(),
                        taskType.getProject().getId()
                )
        )) {
            throw new AuthorizationException("Вы не имеете прав для доступа к этому проекту");
        }
        return mapper.toDto(repository.save(
                Task.builder()
                        .creator(userAccesses.get(0).getId().getUser())
                        .project(taskType.getProject())
                        .taskType(taskType)
                        .taskStatus(resolveInitialStatusForTaskType(taskType))
                        .code(generateCode(taskType.getProject()))
                        .summary(request.summary())
                        .description(request.description())
                        .build()
        ));
    }

    @Override
    public TaskDto assigneeUser(TaskAssigningRequest request, TrillaAuthentication authentication) {
        final var optionalTask = repository.findById(request.taskId());
        if (optionalTask.isEmpty()) {
            throw new DataValidationException("Задача с данным идентификатором не найдена");
        }
        final var task = optionalTask.get();
        final var project = task.getProject();
        final var optionalAssigneeAccess = userAccessRepository.findByIdUserIdAndIdProjectId(
                request.userId(),
                project.getId()
        );
        if (optionalAssigneeAccess.isEmpty()) {
            throw new DataValidationException("Пользователь не имеет доступа к проекту");
        }
        if (!userAccessRepository.existsByIdUserIdAndIdProjectId(
                authentication.id(),
                project.getId()
        )) {
            throw new AuthorizationException("Вы не имеете доступа к данной задаче");
        }
        final var assigneeAccess = optionalAssigneeAccess.get();
        task.setAssignee(assigneeAccess.getId().getUser());
        return mapper.toDto(repository.save(task));
    }

    private TaskStatus resolveInitialStatusForTaskType(TaskType taskType) {
        return taskStatusRepository.findByTaskTypeAndIsInitial(taskType, true).orElseThrow();
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

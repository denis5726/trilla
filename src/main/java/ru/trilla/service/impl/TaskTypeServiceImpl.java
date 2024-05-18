package ru.trilla.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trilla.dto.TaskTypeCreatingRequest;
import ru.trilla.dto.TaskTypeDto;
import ru.trilla.dto.TaskTypeUpdatingRequest;
import ru.trilla.entity.Project;
import ru.trilla.entity.TaskType;
import ru.trilla.exception.DataValidationException;
import ru.trilla.mapper.TaskTypeMapper;
import ru.trilla.repository.ProjectRepository;
import ru.trilla.repository.TaskRepository;
import ru.trilla.repository.TaskTypeRepository;
import ru.trilla.security.TrillaAuthentication;
import ru.trilla.service.TaskTypeService;
import ru.trilla.service.UserAccessAuthorizer;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskTypeServiceImpl implements TaskTypeService {
    private final TaskTypeRepository repository;
    private final TaskTypeMapper mapper;
    private final ProjectRepository projectRepository;
    private final UserAccessAuthorizer authorizer;
    private final TaskRepository taskRepository;

    @Override
    public List<TaskTypeDto> findByProject(UUID projectId, TrillaAuthentication authentication) {
        return mapper.toDtoList(repository.findByProjectId(projectId));
    }

    @Override
    @Transactional
    public TaskTypeDto create(TaskTypeCreatingRequest request, TrillaAuthentication authentication) {
        final var project = projectRepository.findById(request.projectId()).orElseThrow();
        authorizer.checkAccess(authentication.id(), project);
        checkNameIdentity(request.name(), project);
        return mapper.toDto(repository.save(
                TaskType.builder()
                        .name(request.name())
                        .project(project)
                        .build()
        ));
    }

    @Override
    @Transactional
    public TaskTypeDto updateName(TaskTypeUpdatingRequest request, TrillaAuthentication authentication) {
        final var taskType = repository.findById(request.id()).orElseThrow();
        authorizer.checkAccess(authentication.id(), taskType.getProject());
        checkNameIdentity(request.name(), taskType.getProject());
        taskType.setName(request.name());
        return mapper.toDto(repository.save(taskType));
    }

    @Override
    @Transactional
    public void deleteById(UUID taskTypeId, TrillaAuthentication authentication) {
        final var taskType = repository.findById(taskTypeId).orElseThrow();
        if (taskRepository.existsByTaskType(taskType)) {
            log.info("Attempt to delete task type (id={}) with tasks", taskTypeId);
            throw new DataValidationException("Невозможно удалить тип задачи, так как есть задачи принадлежащие к нему");
        }
        repository.delete(taskType);
    }

    private void checkNameIdentity(String name, Project project) {
        if (repository.existsByNameAndProject(name, project)) {
            log.info("Attempt to set non unique task type name: {} in project: {}", name, project.getCode());
            throw new DataValidationException("Тип задачи с таким названием уже существует в проекте");
        }
    }
}

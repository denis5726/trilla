package ru.trilla.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
    public TaskTypeDto create(TaskTypeCreatingRequest request, TrillaAuthentication authentication) {
        final var project = projectRepository.findById(request.projectId()).orElseThrow(() -> {
            log.info("Attempt to create task type with non-existent project (id={})", request.projectId());
            return new DataValidationException("Проект с данным идентификатором не найден");
        });
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
    public TaskTypeDto updateName(TaskTypeUpdatingRequest request, TrillaAuthentication authentication) {
        final var taskType = repository.findById(request.id()).orElseThrow(() -> {
            log.info("Attempt to update non-existent task type (id={})", request.id());
            return new DataValidationException("Тип задачи с данным идентификатором не найден");
        });
        authorizer.checkAccess(authentication.id(), taskType.getProject());
        checkNameIdentity(request.name(), taskType.getProject());
        taskType.setName(request.name());
        return mapper.toDto(repository.save(taskType));
    }

    @Override
    public void deleteById(UUID taskTypeId, TrillaAuthentication authentication) {
        final var taskType = repository.findById(taskTypeId).orElseThrow(() -> {
            log.info("Attempt to update non-existent task type (id={})", taskTypeId);
            return new DataValidationException("Тип задачи с данным идентификатором не найден");
        });
    }

    private void checkNameIdentity(String name, Project project) {
        if (repository.existsByNameAndProject(name, project)) {
            log.info("Attempt to set non unique task type name: {} in project: {}", name, project.getCode());
            throw new DataValidationException("Тип задачи с таким названием уже существует в проекте");
        }
    }
}

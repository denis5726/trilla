package ru.trilla.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.trilla.dto.ProjectCreatingRequest;
import ru.trilla.entity.AdditionalFieldValueType;
import ru.trilla.entity.Project;
import ru.trilla.entity.Role;
import ru.trilla.entity.TaskStatus;
import ru.trilla.entity.TaskStatusTransition;
import ru.trilla.entity.TaskType;
import ru.trilla.entity.User;
import ru.trilla.entity.UserAccess;
import ru.trilla.repository.AdditionalFieldValueTypeRepository;
import ru.trilla.repository.ProjectRepository;
import ru.trilla.repository.TaskStatusRepository;
import ru.trilla.repository.TaskStatusTransitionRepository;
import ru.trilla.repository.TaskTypeRepository;
import ru.trilla.repository.UserAccessRepository;
import ru.trilla.service.ProjectFabric;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectFabricImpl implements ProjectFabric {
    private final ProjectRepository projectRepository;
    private final UserAccessRepository userAccessRepository;
    private final TaskTypeRepository taskTypeRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusTransitionRepository taskStatusTransitionRepository;
    private final AdditionalFieldValueTypeRepository additionalFieldValueTypeRepository;

    @Override
    @Transactional
    public Project createSimpleProject(ProjectCreatingRequest request, User owner) {
        final var project = projectRepository.save(
                Project.builder()
                        .fullName(request.fullName())
                        .code(request.code())
                        .build()
        );
        userAccessRepository.save(
                UserAccess.builder()
                        .id(
                                UserAccess.Id.builder()
                                        .user(owner)
                                        .project(project)
                                        .build()
                        )
                        .role(Role.ADMIN)
                        .build()
        );

        saveSimpleAdditionalFieldValueTypes(project);
        saveSimpleProjectTaskTypes(project);

        return project;
    }

    private void saveSimpleProjectTaskTypes(Project project) {
        saveStoryTaskType(project);
        saveBugTaskType(project);
    }

    private void saveStoryTaskType(Project project) {
        final var story = taskTypeRepository.save(
                TaskType.builder()
                        .project(project)
                        .name("Story")
                        .build()
        );
        final var toDo = saveTaskStatus("To Do", story);
        final var inDiscovery = saveTaskStatus("In Discovery", story);
        final var inProgress = saveTaskStatus("In Progress", story);
        final var readyForTesting = saveTaskStatus("Ready for Testing", story);
        final var testing = saveTaskStatus("Testing", story);
        final var done = saveTaskStatus("Done", story);
        final var canceled = saveTaskStatus("Canceled", story);

        saveTaskStatusTransition(toDo, inDiscovery);
        saveTaskStatusTransition(toDo, inProgress);
        saveTaskStatusTransition(toDo, canceled);


        saveTaskStatusTransition(inDiscovery, toDo);
        saveTaskStatusTransition(inDiscovery, inProgress);
        saveTaskStatusTransition(inDiscovery, canceled);

        saveTaskStatusTransition(inProgress, toDo);
        saveTaskStatusTransition(inProgress, inDiscovery);
        saveTaskStatusTransition(inProgress, readyForTesting);
        saveTaskStatusTransition(inProgress, canceled);


        saveTaskStatusTransition(readyForTesting, inProgress);
        saveTaskStatusTransition(readyForTesting, testing);
        saveTaskStatusTransition(readyForTesting, canceled);

        saveTaskStatusTransition(testing, toDo);
        saveTaskStatusTransition(testing, readyForTesting);
        saveTaskStatusTransition(testing, done);
        saveTaskStatusTransition(testing, canceled);
    }

    // TODO Добавить статусы и переходы
    private void saveBugTaskType(Project project) {
        taskTypeRepository.save(
                TaskType.builder()
                        .project(project)
                        .name("Bug")
                        .build()
        );
    }

    private void saveSimpleAdditionalFieldValueTypes(Project project) {
        additionalFieldValueTypeRepository.saveAll(
                List.of(
                        AdditionalFieldValueType.builder()
                                .project(project)
                                .name("int")
                                .body("int")
                                .build(),
                        AdditionalFieldValueType.builder()
                                .project(project)
                                .name("string")
                                .body("String")
                                .build()
                )
        );
    }

    private TaskStatus saveTaskStatus(String name, TaskType taskType) {
        return taskStatusRepository.save(
                TaskStatus.builder()
                        .taskType(taskType)
                        .name(name)
                        .build()
        );
    }

    private void saveTaskStatusTransition(TaskStatus from, TaskStatus in) {
        taskStatusTransitionRepository.save(
                TaskStatusTransition.builder()
                        .id(
                                TaskStatusTransition.Id.builder()
                                        .from(from)
                                        .in(in)
                                        .build()
                        )
                        .build()
        );
    }
}

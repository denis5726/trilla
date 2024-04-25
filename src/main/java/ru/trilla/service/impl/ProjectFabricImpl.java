package ru.trilla.service.impl;

import org.springframework.stereotype.Component;
import ru.trilla.entity.AdditionalFieldValueType;
import ru.trilla.entity.Project;
import ru.trilla.entity.Role;
import ru.trilla.entity.TaskStatus;
import ru.trilla.entity.TaskStatusTransition;
import ru.trilla.entity.TaskType;
import ru.trilla.entity.User;
import ru.trilla.entity.UserAccess;
import ru.trilla.service.ProjectFabric;

import java.util.Collections;
import java.util.List;

// TODO Здесь же выполнять сохранение всех зависимых объектов
@Component
public class ProjectFabricImpl implements ProjectFabric {

    @Override
    public Project createSimpleProject(User owner) {
        final var project = new Project();
        final var userAccess = UserAccess.builder()
                .user(owner)
                .project(project)
                .role(Role.ADMIN)
                .build();

        project.setAdditionalFieldValueTypes(buildSimpleAdditionalFieldValueTypes(project));
        project.setTaskTypes(buildSimpleProjectTaskTypes(project));
        project.setUserAccesses(Collections.singletonList(userAccess));

        return project;
    }

    private List<TaskType> buildSimpleProjectTaskTypes(Project project) {
        return List.of(buildStoryTaskType(project), buildBugTaskType(project));
    }

    private TaskType buildStoryTaskType(Project project) {
        final var story = TaskType.builder()
                .project(project)
                .name("Story")
                .build();
        final var toDo = buildFromName("To Do", story);
        final var inDiscovery = buildFromName("In Discovery", story);
        final var inProgress = buildFromName("In Progress", story);
        final var readyForTesting = buildFromName("Ready for Testing", story);
        final var testing = buildFromName("Testing", story);
        final var done = buildFromName("Done", story);
        final var canceled = buildFromName("Canceled", story);
        toDo.setTaskStatusTransitions(List.of(
                buildFromTaskStatuses(toDo, inDiscovery),
                buildFromTaskStatuses(toDo, inProgress),
                buildFromTaskStatuses(toDo, canceled)
        ));
        inDiscovery.setTaskStatusTransitions(List.of(
                buildFromTaskStatuses(inDiscovery, toDo),
                buildFromTaskStatuses(inDiscovery, inProgress),
                buildFromTaskStatuses(inDiscovery, canceled)
        ));
        inProgress.setTaskStatusTransitions(List.of(
                buildFromTaskStatuses(inProgress, toDo),
                buildFromTaskStatuses(inProgress, inDiscovery),
                buildFromTaskStatuses(inProgress, readyForTesting),
                buildFromTaskStatuses(inProgress, canceled)
        ));
        readyForTesting.setTaskStatusTransitions(List.of(
                buildFromTaskStatuses(readyForTesting, inProgress),
                buildFromTaskStatuses(readyForTesting, testing),
                buildFromTaskStatuses(readyForTesting, canceled)
        ));
        testing.setTaskStatusTransitions(List.of(
                buildFromTaskStatuses(testing, toDo),
                buildFromTaskStatuses(testing, readyForTesting),
                buildFromTaskStatuses(testing, done),
                buildFromTaskStatuses(testing, canceled)
        ));
        story.setTaskStatuses(List.of(toDo, inDiscovery, inProgress, readyForTesting, testing, done, canceled));

        return story;
    }

    // TODO Добавить статусы и переходы
    private TaskType buildBugTaskType(Project project) {
        return TaskType.builder()
                .project(project)
                .name("Bug")
                .build();
    }

    private List<AdditionalFieldValueType> buildSimpleAdditionalFieldValueTypes(Project project) {
        return List.of(
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
        );
    }

    private TaskStatus buildFromName(String name, TaskType taskType) {
        return TaskStatus.builder()
                .taskType(taskType)
                .name(name)
                .build();
    }

    private TaskStatusTransition buildFromTaskStatuses(TaskStatus from, TaskStatus in) {
        return TaskStatusTransition.builder()
                .from(from)
                .in(in)
                .build();
    }
}

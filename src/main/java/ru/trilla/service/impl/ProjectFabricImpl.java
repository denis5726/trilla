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

import java.util.List;

@Component
public class ProjectFabricImpl implements ProjectFabric {

    @Override
    public Project createSimpleProject(User owner) {
        final var project = new Project();
        final var userAccess = UserAccess.builder()
                .id(
                        UserAccess.Id.builder()
                                .user(owner)
                                .project(project)
                                .build()
                )
                .role(Role.ADMIN)
                .build();
        project.setAdditionalFieldValueTypes(buildSimpleAdditionalFieldValueTypes());
        project.setTaskTypes(buildSimpleProjectTaskTypes(project.getAdditionalFieldValueTypes()));
        return null;
    }

    private List<TaskType> buildSimpleProjectTaskTypes(List<AdditionalFieldValueType> additionalFieldValueTypes) {
        return List.of(buildStoryTaskType(additionalFieldValueTypes), buildBugTaskType(additionalFieldValueTypes));
    }

    private TaskType buildStoryTaskType(List<AdditionalFieldValueType> additionalFieldValueTypes) {
        final var story = TaskType.builder()
                .name("Story")
                .build();
        final var toDo = buildFromName("To Do");
        final var inDiscovery = buildFromName("In Discovery");
        final var inProgress = buildFromName("In Progress");
        final var readyForTesting = buildFromName("Ready for Testing");
        final var testing = buildFromName("Testing");
        final var done = buildFromName("Done");
        final var canceled = buildFromName("Canceled");
        toDo.setTaskStatusTransitions(List.of(
                buildFromTaskStatuses(toDo, inDiscovery),
                buildFromTaskStatuses(toDo, inProgress),
                buildFromTaskStatuses(toDo, canceled)
        ));
        inDiscovery.setTaskStatusTransitions(List.of(
                buildFromTaskStatuses(inDiscovery, inDiscovery),
                buildFromTaskStatuses(inDiscovery, inProgress),
                buildFromTaskStatuses(inDiscovery, canceled)
        ));

        return null;
    }

    private TaskType buildBugTaskType(List<AdditionalFieldValueType> additionalFieldValueTypes) {
        return null;
    }

    private List<AdditionalFieldValueType> buildSimpleAdditionalFieldValueTypes() {
        return List.of(
                AdditionalFieldValueType.builder()
                        .name("int")
                        .body("int")
                        .build(),
                AdditionalFieldValueType.builder()
                        .name("string")
                        .body("String")
                        .build()
        );
    }

    private TaskStatus buildFromName(String name) {
        return TaskStatus.builder()
                .name(name)
                .build();
    }

    private TaskStatusTransition buildFromTaskStatuses(TaskStatus from, TaskStatus in) {
        return TaskStatusTransition.builder()
                .id(
                        TaskStatusTransition.Id.builder()
                                .from(from)
                                .in(in)
                                .build()
                )
                .build();
    }
}

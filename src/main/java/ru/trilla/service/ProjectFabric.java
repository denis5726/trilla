package ru.trilla.service;

import ru.trilla.dto.ProjectCreatingRequest;
import ru.trilla.entity.Project;
import ru.trilla.entity.User;

public interface ProjectFabric {

    Project createSimpleProject(ProjectCreatingRequest request, User owner);
}

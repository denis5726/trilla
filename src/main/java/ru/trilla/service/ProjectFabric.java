package ru.trilla.service;

import ru.trilla.entity.Project;
import ru.trilla.entity.User;

public interface ProjectFabric {

    Project createSimpleProject(User owner);
}

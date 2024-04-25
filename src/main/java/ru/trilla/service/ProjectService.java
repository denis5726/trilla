package ru.trilla.service;

import ru.trilla.dto.ProjectCreatingRequest;
import ru.trilla.dto.ProjectDto;
import ru.trilla.security.TrillaAuthentication;

import java.util.List;

public interface ProjectService {

    List<ProjectDto> findAllForCurrentUser(TrillaAuthentication authentication);

    ProjectDto create(ProjectCreatingRequest request, TrillaAuthentication authentication);
}

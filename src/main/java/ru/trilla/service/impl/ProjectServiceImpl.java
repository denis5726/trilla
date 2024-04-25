package ru.trilla.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.trilla.dto.ProjectCreatingRequest;
import ru.trilla.dto.ProjectDto;
import ru.trilla.exception.ResourceAlreadyExistsException;
import ru.trilla.mapper.ProjectMapper;
import ru.trilla.repository.ProjectRepository;
import ru.trilla.repository.UserRepository;
import ru.trilla.security.TrillaAuthentication;
import ru.trilla.service.ProjectFabric;
import ru.trilla.service.ProjectService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository repository;
    private final ProjectMapper mapper;
    private final ProjectFabric fabric;
    private final UserRepository userRepository;

    @Override
    public List<ProjectDto> findAllForCurrentUser(TrillaAuthentication authentication) {
        return mapper.toDtoList(repository.findByUserAccessesUserId(authentication.id()));
    }

    @Override
    public ProjectDto create(ProjectCreatingRequest request, TrillaAuthentication authentication) {
        if (repository.existsByCode(request.code())) {
            throw new ResourceAlreadyExistsException("Проект с данным кодом уже существует");
        }
        final var project = fabric.createSimpleProject(userRepository.findById(authentication.id()).orElseThrow());
        mapper.addCreationRequestProperties(project, request);
        return mapper.toDto(repository.save(project));
    }
}

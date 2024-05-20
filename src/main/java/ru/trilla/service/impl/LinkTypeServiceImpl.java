package ru.trilla.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trilla.dto.DetailedLinkTypeDto;
import ru.trilla.dto.LinkTypeCreatingRequest;
import ru.trilla.dto.LinkTypeUpdatingRequest;
import ru.trilla.entity.LinkType;
import ru.trilla.entity.Project;
import ru.trilla.entity.Role;
import ru.trilla.exception.DataValidationException;
import ru.trilla.mapper.LinkTypeMapper;
import ru.trilla.repository.LinkRepository;
import ru.trilla.repository.LinkTypeRepository;
import ru.trilla.repository.TaskTypeRepository;
import ru.trilla.security.TrillaAuthentication;
import ru.trilla.service.LinkTypeService;
import ru.trilla.service.UserAccessAuthorizer;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class LinkTypeServiceImpl implements LinkTypeService {
    private final LinkTypeRepository repository;
    private final LinkTypeMapper mapper;
    private final TaskTypeRepository taskTypeRepository;
    private final UserAccessAuthorizer authorizer;
    private final LinkRepository linkRepository;

    @Override
    public List<DetailedLinkTypeDto> findByTaskTypeIn(UUID taskTypeFromId, TrillaAuthentication authentication) {
        final var taskTypeFrom = taskTypeRepository.findById(taskTypeFromId).orElseThrow();
        authorizer.checkAccess(authentication.id(), taskTypeFrom.getProject());
        return mapper.toDtoList(repository.findByFrom(taskTypeFrom));
    }

    @Override
    @Transactional
    public DetailedLinkTypeDto create(LinkTypeCreatingRequest request, TrillaAuthentication authentication) {
        final var taskTypeFrom = taskTypeRepository.findById(request.taskTypeFromId()).orElseThrow();
        final var project = taskTypeFrom.getProject();
        authorizer.checkAccess(authentication.id(), project, Role.ADMIN);
        checkNameIdentity(request.name(), project);
        final var taskTypeIn = taskTypeRepository.findById(request.taskTypeInId()).orElseThrow();
        final var linkType = LinkType.builder()
                .name(request.name())
                .from(taskTypeFrom)
                .in(taskTypeIn)
                .build();
        return mapper.toDto(repository.save(linkType));
    }

    @Override
    @Transactional
    public DetailedLinkTypeDto updateName(LinkTypeUpdatingRequest request, TrillaAuthentication authentication) {
        final var linkType = repository.findById(request.id()).orElseThrow();
        final var project = linkType.getFrom().getProject();
        authorizer.checkAccess(authentication.id(), project, Role.ADMIN);
        checkNameIdentity(request.name(), project);
        linkType.setName(request.name());
        return mapper.toDto(repository.save(linkType));
    }

    @Override
    @Transactional
    public void deleteById(UUID linkTypeId, TrillaAuthentication authentication) {
        final var linkType = repository.findById(linkTypeId).orElseThrow();
        authorizer.checkAccess(authentication.id(), linkType.getFrom().getProject(), Role.ADMIN);
        linkRepository.deleteByLinkType(linkType);
        repository.delete(linkType);
    }

    private void checkNameIdentity(String name, Project project) {
        if (repository.existsByFromProjectAndName(project, name)) {
            log.info("Attempt to update link type to existent name: {}, in projectId = {}", name, project.getId());
            throw new DataValidationException("Тип ссылки с таким именем уже существует в проекте");
        }
    }
}

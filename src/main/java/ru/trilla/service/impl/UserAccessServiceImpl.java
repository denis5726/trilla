package ru.trilla.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trilla.dto.UserAccessCreatingRequest;
import ru.trilla.dto.UserAccessDeletingRequest;
import ru.trilla.dto.UserAccessDto;
import ru.trilla.entity.Role;
import ru.trilla.entity.UserAccess;
import ru.trilla.exception.AuthorizationException;
import ru.trilla.exception.DataValidationException;
import ru.trilla.mapper.UserAccessMapper;
import ru.trilla.repository.ProjectRepository;
import ru.trilla.repository.UserAccessRepository;
import ru.trilla.repository.UserRepository;
import ru.trilla.security.TrillaAuthentication;
import ru.trilla.service.UserAccessAuthorizer;
import ru.trilla.service.UserAccessService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAccessServiceImpl implements UserAccessService {
    private final UserAccessRepository repository;
    private final UserAccessMapper mapper;
    private final UserAccessAuthorizer authorizer;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public List<UserAccessDto> findMyAccesses(TrillaAuthentication authentication) {
        return mapper.toDtoList(repository.findByIdUserId(authentication.id()));
    }

    @Override
    public List<UserAccessDto> findGrantedUsers(UUID projectId, TrillaAuthentication authentication) {
        authorizer.checkAccessByProject(authentication.id(), projectId);
        return mapper.toDtoList(repository.findByIdProjectId(projectId));
    }

    @Override
    @Transactional
    public UserAccessDto createUserAccess(UserAccessCreatingRequest request, TrillaAuthentication authentication) {
        checkSelfChanging(request.userId(), authentication);
        final var user = userRepository.findById(request.userId()).orElseThrow(() ->
                new DataValidationException("Пользователь с данным идентификатором не найден")
        );
        final var project = projectRepository.findById(request.projectId()).orElseThrow(() ->
                new DataValidationException("Проект с указанным идентификатором не найден")
        );
        authorizer.checkAccess(authentication.id(), project, Role.ADMIN);
        return mapper.toDto(repository.save(
                UserAccess.builder()
                        .id(
                                UserAccess.Id.builder()
                                        .user(user)
                                        .project(project)
                                        .build()
                        )
                        .role(request.role())
                        .build()
        ));
    }

    @Override
    @Transactional
    public void deleteUserAccess(UserAccessDeletingRequest request, TrillaAuthentication authentication) {
        checkSelfChanging(request.userId(), authentication);
        authorizer.checkAccess(
                authentication.id(),
                projectRepository.findById(request.projectId()).orElseThrow(() ->
                        new DataValidationException("Проект с данным идентификатором не найден")
                ),
                Role.ADMIN
        );
        repository.deleteByIdUserIdAndIdProjectId(request.userId(), request.projectId());
    }

    private void checkSelfChanging(UUID userId, TrillaAuthentication authentication) {
        if (Objects.equals(authentication.id(), userId)) {
            throw new AuthorizationException("Невозможно изменить свой доступ на проект");
        }
    }
}

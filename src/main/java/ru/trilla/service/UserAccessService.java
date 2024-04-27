package ru.trilla.service;

import ru.trilla.dto.UserAccessCreatingRequest;
import ru.trilla.dto.UserAccessDeletingRequest;
import ru.trilla.dto.UserAccessDto;
import ru.trilla.security.TrillaAuthentication;

import java.util.List;
import java.util.UUID;

public interface UserAccessService {

    List<UserAccessDto> findMyAccesses(TrillaAuthentication authentication);

    List<UserAccessDto> findGrantedUsers(UUID projectId, TrillaAuthentication authentication);

    UserAccessDto createUserAccess(UserAccessCreatingRequest request, TrillaAuthentication authentication);

    void deleteUserAccess(UserAccessDeletingRequest request, TrillaAuthentication authentication);
}

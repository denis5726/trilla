package ru.trilla.service;

import ru.trilla.dto.DetailedLinkTypeDto;
import ru.trilla.dto.LinkTypeCreatingRequest;
import ru.trilla.dto.LinkTypeUpdatingRequest;
import ru.trilla.security.TrillaAuthentication;

import java.util.List;
import java.util.UUID;

public interface LinkTypeService {

    List<DetailedLinkTypeDto> findByTaskTypeIn(UUID taskTypeFromId, TrillaAuthentication authentication);

    DetailedLinkTypeDto create(LinkTypeCreatingRequest request, TrillaAuthentication authentication);

    DetailedLinkTypeDto updateName(LinkTypeUpdatingRequest request, TrillaAuthentication authentication);

    void deleteById(UUID linkTypeId, TrillaAuthentication authentication);
}

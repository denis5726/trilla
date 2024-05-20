package ru.trilla.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.trilla.config.OpenApiDefinition;
import ru.trilla.dto.DetailedLinkTypeDto;
import ru.trilla.dto.LinkTypeCreatingRequest;
import ru.trilla.dto.LinkTypeUpdatingRequest;
import ru.trilla.security.TrillaAuthentication;
import ru.trilla.service.LinkTypeService;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = OpenApiDefinition.MAIN_SECURITY_SCHEME)
@RequestMapping("/linkTypes")
@RequiredArgsConstructor
public class LinkTypeController {
    private final LinkTypeService service;

    @GetMapping
    public List<DetailedLinkTypeDto> findByTaskType(@RequestParam UUID taskTypeFromId, TrillaAuthentication authentication) {
        return service.findByTaskTypeIn(taskTypeFromId, authentication);
    }

    @PostMapping
    public DetailedLinkTypeDto create(@RequestBody LinkTypeCreatingRequest request, TrillaAuthentication authentication) {
        return service.create(request, authentication);
    }

    @PutMapping
    public DetailedLinkTypeDto updateName(@RequestBody LinkTypeUpdatingRequest request, TrillaAuthentication authentication) {
        return service.updateName(request, authentication);
    }

    @DeleteMapping("/{linkTypeId}")
    public void deleteById(@PathVariable UUID linkTypeId, TrillaAuthentication authentication) {
        service.deleteById(linkTypeId, authentication);
    }
}

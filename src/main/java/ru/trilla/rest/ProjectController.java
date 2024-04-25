package ru.trilla.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.trilla.dto.ProjectCreatingRequest;
import ru.trilla.dto.ProjectDto;
import ru.trilla.security.TrillaAuthentication;
import ru.trilla.service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService service;

    @GetMapping
    public List<ProjectDto> findAll(TrillaAuthentication authentication) {
        return service.findAllForCurrentUser(authentication);
    }

    @PostMapping
    public ProjectDto create(@RequestBody ProjectCreatingRequest request, TrillaAuthentication authentication) {
        return service.create(request, authentication);
    }
}

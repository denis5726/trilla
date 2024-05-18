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
import ru.trilla.dto.DetailedTaskStatusDto;
import ru.trilla.dto.TaskStatusCreatingRequest;
import ru.trilla.dto.TaskStatusUpdatingRequest;
import ru.trilla.security.TrillaAuthentication;
import ru.trilla.service.TaskStatusService;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = OpenApiDefinition.MAIN_SECURITY_SCHEME)
@RequestMapping("/taskStatuses")
@RequiredArgsConstructor
public class TaskStatusController {
    private final TaskStatusService service;

    @GetMapping
    public List<DetailedTaskStatusDto> findByTaskType(@RequestParam UUID taskTypeId, TrillaAuthentication authentication) {
        return service.findByTaskType(taskTypeId, authentication);
    }

    @PostMapping
    public DetailedTaskStatusDto create(@RequestBody TaskStatusCreatingRequest request, TrillaAuthentication authentication) {
        return service.create(request, authentication);
    }

    @PutMapping
    public DetailedTaskStatusDto update(@RequestBody TaskStatusUpdatingRequest request, TrillaAuthentication authentication) {
        return service.update(request, authentication);
    }

    @DeleteMapping("/{taskStatusId}")
    public void delete(@PathVariable UUID taskStatusId, TrillaAuthentication authentication) {
        service.delete(taskStatusId, authentication);
    }
}

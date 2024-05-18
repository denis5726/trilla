package ru.trilla.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.trilla.dto.TaskTypeCreatingRequest;
import ru.trilla.dto.TaskTypeDto;
import ru.trilla.dto.TaskTypeUpdatingRequest;
import ru.trilla.security.TrillaAuthentication;
import ru.trilla.service.TaskTypeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/taskTypes")
@RequiredArgsConstructor
public class TaskTypeController {
    private final TaskTypeService taskTypeService;

    @GetMapping
    public List<TaskTypeDto> findByProject(@RequestParam UUID projectId, TrillaAuthentication authentication) {
        return taskTypeService.findByProject(projectId, authentication);
    }

    @PostMapping
    public TaskTypeDto create(@RequestBody TaskTypeCreatingRequest request, TrillaAuthentication authentication) {
        return taskTypeService.create(request, authentication);
    }

    @PatchMapping
    public TaskTypeDto updateName(@RequestBody TaskTypeUpdatingRequest request, TrillaAuthentication authentication) {
        return taskTypeService.updateName(request, authentication);
    }

    @DeleteMapping("/{taskTypeId}")
    public void deleteById(@PathVariable UUID taskTypeId, TrillaAuthentication authentication) {
        taskTypeService.deleteById(taskTypeId, authentication);
    }
}

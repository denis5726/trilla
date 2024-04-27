package ru.trilla.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.trilla.dto.TaskAssigningRequest;
import ru.trilla.dto.TaskCreatingRequest;
import ru.trilla.dto.TaskDto;
import ru.trilla.security.TrillaAuthentication;
import ru.trilla.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService service;

    @GetMapping("/onMe")
    public List<TaskDto> findActualAndAssigneeOnMe(TrillaAuthentication authentication) {
        return service.findActualAndAssigneeOnMe(authentication);
    }

    @PostMapping
    public TaskDto create(@RequestBody TaskCreatingRequest request, TrillaAuthentication authentication) {
        return service.create(request, authentication);
    }

    @PatchMapping("/assign")
    public TaskDto assignUser(@RequestBody TaskAssigningRequest request, TrillaAuthentication authentication) {
        return service.assigneeUser(request, authentication);
    }
}

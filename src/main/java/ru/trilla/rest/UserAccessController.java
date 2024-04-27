package ru.trilla.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.trilla.dto.UserAccessCreatingRequest;
import ru.trilla.dto.UserAccessDeletingRequest;
import ru.trilla.dto.UserAccessDto;
import ru.trilla.security.TrillaAuthentication;
import ru.trilla.service.UserAccessService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accesses")
@RequiredArgsConstructor
public class UserAccessController {
    private final UserAccessService service;

    @GetMapping
    public List<UserAccessDto> findMyAccesses(TrillaAuthentication authentication) {
        return service.findMyAccesses(authentication);
    }

    @GetMapping("/project")
    public List<UserAccessDto> findGrantedUsers(@RequestParam UUID value, TrillaAuthentication authentication) {
        return service.findGrantedUsers(value, authentication);
    }

    @PutMapping
    public UserAccessDto createUserAccess(
            @RequestBody UserAccessCreatingRequest request,
            TrillaAuthentication authentication
    ) {
        return service.createUserAccess(request, authentication);
    }

    @DeleteMapping
    public void deleteUserAccess(@RequestBody UserAccessDeletingRequest request, TrillaAuthentication authentication) {
        service.deleteUserAccess(request, authentication);
    }
}

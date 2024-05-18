package ru.trilla.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.trilla.config.OpenApiDefinition;
import ru.trilla.dto.SignInRequest;
import ru.trilla.dto.SignUpRequest;
import ru.trilla.dto.UserDto;
import ru.trilla.service.UserService;

@RestController
@SecurityRequirement(name = OpenApiDefinition.MAIN_SECURITY_SCHEME)
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/signUp")
    public UserDto signUp(@RequestBody SignUpRequest request) {
        return service.signUp(request);
    }

    @PostMapping("/signIn")
    public String signIn(@RequestBody SignInRequest request) {
        return service.signIn(request);
    }
}

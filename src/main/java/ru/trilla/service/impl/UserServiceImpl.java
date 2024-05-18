package ru.trilla.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.trilla.dto.SignInRequest;
import ru.trilla.dto.SignUpRequest;
import ru.trilla.dto.UserDto;
import ru.trilla.entity.User;
import ru.trilla.exception.AuthenticationException;
import ru.trilla.exception.DataValidationException;
import ru.trilla.mapper.UserMapper;
import ru.trilla.repository.UserAccessRepository;
import ru.trilla.repository.UserRepository;
import ru.trilla.security.TokenProvider;
import ru.trilla.security.TrillaAuthentication;
import ru.trilla.service.UserService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final UserAccessRepository userAccessRepository;

    @Override
    public UserDto signUp(SignUpRequest request) {
        final var user = mapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        return mapper.toDto(repository.save(user));
    }

    @Override
    public String signIn(SignInRequest request) {
        final var user = repository.findByEmail(request.email()).orElseThrow(() -> {
            log.info("Attempt to login with non existent user (email={})", request.email());
            return new DataValidationException("Пользователь с данным email не найден");
        });
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            log.info("Attempt to login with wrong password");
            throw new AuthenticationException("Пароль не подходит");
        }
        return tokenProvider.getToken(new TrillaAuthentication(
                user.getId(),
                buildRoles(user)
        ));
    }

    private List<TrillaAuthentication.ProjectRole> buildRoles(User user) {
        return userAccessRepository.findByIdUserId(user.getId()).stream()
                .map(userAccess -> new TrillaAuthentication.ProjectRole(
                        userAccess.getId().getProject().getId(),
                        userAccess.getRole()
                ))
                .toList();
    }
}

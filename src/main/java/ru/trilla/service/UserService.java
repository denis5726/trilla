package ru.trilla.service;

import ru.trilla.dto.SignInRequest;
import ru.trilla.dto.SignUpRequest;
import ru.trilla.dto.UserDto;

public interface UserService {

    UserDto signUp(SignUpRequest request);

    String signIn(SignInRequest request);
}

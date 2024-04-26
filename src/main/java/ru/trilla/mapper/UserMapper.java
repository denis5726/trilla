package ru.trilla.mapper;

import org.mapstruct.Mapper;
import ru.trilla.dto.SignUpRequest;
import ru.trilla.dto.UserDto;
import ru.trilla.entity.User;

@Mapper
public interface UserMapper {

    User toEntity(SignUpRequest request);

    UserDto toDto(User user);
}

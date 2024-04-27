package ru.trilla.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.trilla.dto.UserAccessDto;
import ru.trilla.entity.UserAccess;

import java.util.List;

@Mapper
public interface UserAccessMapper {

    @Mapping(target = "projectFullName", source = "id.project.fullName")
    @Mapping(target = "projectCode", source = "id.project.code")
    @Mapping(target = "projectId", source = "id.project.id")
    @Mapping(target = "userId", source = "id.user.id")
    @Mapping(target = "lastName", source = "id.user.lastName")
    @Mapping(target = "firstName", source = "id.user.firstName")
    @Mapping(target = "email", source = "id.user.email")
    UserAccessDto toDto(UserAccess userAccess);

    List<UserAccessDto> toDtoList(List<UserAccess> userAccesses);
}

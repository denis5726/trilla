package ru.trilla.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.trilla.dto.TaskTypeDto;
import ru.trilla.entity.TaskType;

import java.util.List;

@Mapper
public interface TaskTypeMapper {

    @Mapping(target = "projectId", source = "project.id")
    TaskTypeDto toDto(TaskType entity);

    List<TaskTypeDto> toDtoList(List<TaskType> entities);
}

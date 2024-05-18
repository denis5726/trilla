package ru.trilla.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.trilla.dto.DetailedTaskStatusDto;
import ru.trilla.dto.TaskStatusCreatingRequest;
import ru.trilla.dto.TaskStatusUpdatingRequest;
import ru.trilla.entity.TaskStatus;

import java.util.List;

@Mapper
public interface TaskStatusMapper {

    DetailedTaskStatusDto toDto(TaskStatus entity);

    List<DetailedTaskStatusDto> toDtoList(List<TaskStatus> entities);

    @Mapping(target = "taskType", ignore = true)
    @Mapping(target = "id", ignore = true)
    TaskStatus toEntity(TaskStatusCreatingRequest request);

    @Mapping(target = "taskType", ignore = true)
    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget TaskStatus entity, TaskStatusUpdatingRequest request);
}

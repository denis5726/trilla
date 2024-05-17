package ru.trilla.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.trilla.dto.TaskDto;
import ru.trilla.entity.Task;

import java.util.List;

@Mapper
public interface TaskMapper {

    @Mapping(target = "type", source = "taskType.name")
    @Mapping(target = "status", source = "taskStatus.name")
    @Mapping(target = "assigneeInfo.lastName", source = "assignee.lastName")
    @Mapping(target = "assigneeInfo.firstName", source = "assignee.firstName")
    TaskDto toDto(Task task);

    List<TaskDto> toDtoList(List<Task> tasks);
}

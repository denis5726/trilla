package ru.trilla.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.trilla.dto.ProjectCreatingRequest;
import ru.trilla.dto.ProjectDto;
import ru.trilla.entity.Project;

import java.util.List;

@Mapper
public interface ProjectMapper {

    ProjectDto toDto(Project project);

    List<ProjectDto> toDtoList(List<Project> projects);

    void addCreationRequestProperties(@MappingTarget Project project, ProjectCreatingRequest request);
}

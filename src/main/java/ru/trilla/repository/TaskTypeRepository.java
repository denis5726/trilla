package ru.trilla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.trilla.entity.Project;
import ru.trilla.entity.TaskType;

import java.util.List;
import java.util.UUID;

public interface TaskTypeRepository extends JpaRepository<TaskType, UUID> {

    List<TaskType> findByProjectId(UUID projectId);

    boolean existsByNameAndProject(String name, Project project);
}

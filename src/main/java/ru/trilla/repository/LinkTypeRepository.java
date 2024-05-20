package ru.trilla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.trilla.entity.LinkType;
import ru.trilla.entity.Project;
import ru.trilla.entity.TaskType;

import java.util.List;
import java.util.UUID;

public interface LinkTypeRepository extends JpaRepository<LinkType, UUID> {

    List<LinkType> findByFrom(TaskType taskType);

    boolean existsByFromProjectAndName(Project project, String name);
}

package ru.trilla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.trilla.entity.TaskType;

import java.util.UUID;

public interface TaskTypeRepository extends JpaRepository<TaskType, UUID> {
}

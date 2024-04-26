package ru.trilla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.trilla.entity.TaskStatus;

import java.util.UUID;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, UUID> {
}

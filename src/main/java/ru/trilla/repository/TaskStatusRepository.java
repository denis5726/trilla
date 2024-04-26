package ru.trilla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.trilla.entity.TaskStatus;
import ru.trilla.entity.TaskType;

import java.util.Optional;
import java.util.UUID;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, UUID> {

    Optional<TaskStatus> findByTaskTypeAndIsInitial(TaskType taskType, boolean isInitial);
}

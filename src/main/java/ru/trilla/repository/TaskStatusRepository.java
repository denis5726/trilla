package ru.trilla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.trilla.entity.TaskStatus;
import ru.trilla.entity.TaskType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, UUID> {

    Optional<TaskStatus> findByTaskTypeAndIsInitial(TaskType taskType, boolean isInitial);

    List<TaskStatus> findByTaskType(TaskType taskType);

    boolean existsByTaskTypeAndName(TaskType taskType, String name);

    boolean existsByTaskTypeAndIsInitial(TaskType taskType, boolean isInitial);
}

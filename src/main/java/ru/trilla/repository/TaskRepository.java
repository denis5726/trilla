package ru.trilla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.trilla.entity.Project;
import ru.trilla.entity.Task;
import ru.trilla.entity.TaskType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByAssigneeIdAndTaskStatusIsOpened(UUID assigneeId, boolean isOpened);

    Optional<Task> findFirstByProjectOrderByCodeDesc(Project project);

    boolean existsByTaskType(TaskType taskType);
}

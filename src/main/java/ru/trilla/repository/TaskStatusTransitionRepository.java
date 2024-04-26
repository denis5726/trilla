package ru.trilla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.trilla.entity.TaskStatusTransition;

public interface TaskStatusTransitionRepository extends JpaRepository<TaskStatusTransition, TaskStatusTransition.Id> {
}

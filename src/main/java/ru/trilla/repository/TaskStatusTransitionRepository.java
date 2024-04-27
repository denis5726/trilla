package ru.trilla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.trilla.entity.TaskStatus;
import ru.trilla.entity.TaskStatusTransition;

import java.util.List;

public interface TaskStatusTransitionRepository extends JpaRepository<TaskStatusTransition, TaskStatusTransition.Id> {

    List<TaskStatusTransition> findByIdFrom(TaskStatus taskStatus);
}

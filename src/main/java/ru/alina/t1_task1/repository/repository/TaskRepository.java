package ru.alina.t1_task1.repository.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alina.t1_task1.repository.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{
}

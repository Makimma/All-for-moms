package ru.hse.moms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.moms.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}

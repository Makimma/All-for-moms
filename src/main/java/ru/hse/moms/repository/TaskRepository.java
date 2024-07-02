package ru.hse.moms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.moms.entity.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTaskSetterId(Long userId);
    List<Task> findByTaskGetterId(Long userId);
}

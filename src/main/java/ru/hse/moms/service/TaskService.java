package ru.hse.moms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse.moms.entity.Task;
import ru.hse.moms.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task saveOrUpdateTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getTasksByTaskSetterId(Long userId) {
        return taskRepository.findByTaskSetterId(userId);
    }

    public List<Task> getTasksByTaskGetterId(Long userId) {
        return taskRepository.findByTaskGetterId(userId);
    }
}
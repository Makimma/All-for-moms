package ru.hse.moms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.moms.request.TaskRequest;
import ru.hse.moms.response.TaskResponse;
import ru.hse.moms.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest taskRequest) {
        TaskResponse taskResponse = taskService.createTask(taskRequest);
        return ResponseEntity.ok(taskResponse);
    }

    @GetMapping("/getter/{userId}")
    public ResponseEntity<List<TaskResponse>> getTasksByTaskGetterId(@PathVariable Long userId) {
        List<TaskResponse> taskResponses = taskService.getTasksByTaskGetterId(userId);
        return ResponseEntity.ok(taskResponses);
    }

    @GetMapping("/setter/{userId}")
    public ResponseEntity<List<TaskResponse>> getTasksByTaskSetterId(@PathVariable Long userId) {
        List<TaskResponse> taskResponses = taskService.getTasksByTaskSetterId(userId);
        return ResponseEntity.ok(taskResponses);
    }

    @PutMapping("/complete/{taskId}")
    public ResponseEntity<Void> completeTask(@PathVariable Long taskId) {
        taskService.completeTask(taskId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        TaskResponse taskResponse = taskService.getTaskById(id);
        return ResponseEntity.ok(taskResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest) {
        TaskResponse taskResponse = taskService.updateTask(id, taskRequest);
        return ResponseEntity.ok(taskResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }
}

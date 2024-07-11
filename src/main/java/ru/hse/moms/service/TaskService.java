package ru.hse.moms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.moms.entity.Family;
import ru.hse.moms.entity.Task;
import ru.hse.moms.entity.User;
import ru.hse.moms.exception.FamilyNotFound;
import ru.hse.moms.exception.TaskNotFoundException;
import ru.hse.moms.exception.UserNotFoundException;
import ru.hse.moms.repository.FamilyRepository;
import ru.hse.moms.repository.TaskRepository;
import ru.hse.moms.repository.UserRepository;
import ru.hse.moms.request.TaskRequest;
import ru.hse.moms.response.TaskResponse;
import ru.hse.moms.security.AuthUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;

    private void taskCheck(TaskRequest taskRequest, Family family, User setter, User getter) {
        //Одна семья
        if (!family.getMembers().contains(getter)) {
            throw new AccessDeniedException("Access denied");
        }

        //Задание за баллы
        if (taskRequest.getRewardPoint() != 0) {
            if (!family.getHosts().contains(setter)) {
                throw new AccessDeniedException("Not host");
            }
        }

        if (new Date().after(taskRequest.getEndDate())) {
            throw new IllegalArgumentException("The end date must be in the future.");
        }

        if (taskRequest.getEndDate().before(taskRequest.getStartDate())) {
            throw new IllegalArgumentException("The end date must be after the start date.");
        }
    }

    @Transactional
    public TaskResponse createTask(TaskRequest taskRequest) {
        User setter = userRepository.findById(Objects.requireNonNull(AuthUtils.getCurrentId()))
                .orElseThrow(() -> new RuntimeException("User not found"));
        User getter = userRepository.findById(taskRequest.getTaskGetter())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Family family = familyRepository.findByMembersContaining(setter)
                .orElseThrow(() -> new FamilyNotFound("Family not found"));

        taskCheck(taskRequest, family, setter, getter);

        Task task = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .startDate(taskRequest.getStartDate())
                .endDate(taskRequest.getEndDate())
                .taskGetter(getter)
                .taskSetter(setter)
                .rewardPoint(taskRequest.getRewardPoint())
                .isCompleted(false)
                .build();
        taskRepository.save(task);

        return toResponse(task);
    }


    @Transactional
    public void completeTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        task.setCompleted(true);

        if (!task.getTaskSetter().getId().equals(AuthUtils.getCurrentId())) {
            throw new AccessDeniedException("Not allowed to complete task");
        }

        userService.addPoints(task);
        task.setCompleted(true);
        taskRepository.save(task);
    }

    private TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .taskGetter(task.getTaskGetter().getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .rewardPoint(task.getRewardPoint())
                .id(task.getId())
                .isRecurring(task.isRecurring())
                .isCompleted(task.isCompleted())
                .recurrenceInterval(task.getRecurrenceInterval())
                .build();
    }

    @Transactional
    public void handleRecurringTasks() {
        Date currentDate = new Date();
        List<Task> recurringTasks = taskRepository.findAll().stream()
                .filter(task -> task.isRecurring() && task.getEndDate().before(currentDate))
                .toList();

        for (Task task : recurringTasks) {
            createRecurringTask(task);
        }
    }

    @Transactional
    public void createRecurringTask(Task task) {
        Task newTask = new Task();
        newTask.setDescription(task.getDescription());
        newTask.setTitle(task.getTitle());
        newTask.setRecurring(true);
        newTask.setRecurrenceInterval(task.getRecurrenceInterval());
        newTask.setTaskGetter(task.getTaskGetter());

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(task.getStartDate());

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(task.getEndDate());

        switch (task.getRecurrenceInterval()) {
            case DAILY:
                calendarStart.add(Calendar.DAY_OF_YEAR, 1);
                calendarEnd.add(Calendar.DAY_OF_YEAR, 1);
                break;
            case WEEKLY:
                calendarStart.add(Calendar.WEEK_OF_YEAR, 1);
                calendarEnd.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case MONTHLY:
                calendarStart.add(Calendar.MONTH, 1);
                calendarEnd.add(Calendar.MONTH, 1);
                break;
            case YEARLY:
                calendarStart.add(Calendar.YEAR, 1);
                calendarEnd.add(Calendar.YEAR, 1);
                break;
        }

        task.setCompleted(true);
        taskRepository.save(task);

        newTask.setStartDate(calendarStart.getTime());
        newTask.setEndDate(calendarEnd.getTime());
        taskRepository.save(newTask);
    }

    @Transactional
    public TaskResponse updateTask(Long taskId, TaskRequest taskRequest) {
        User setter = userRepository.findById(Objects.requireNonNull(AuthUtils.getCurrentId()))
                .orElseThrow(() -> new RuntimeException("User not found"));
        Family family = familyRepository.findByMembersContaining(setter)
                .orElseThrow(() -> new FamilyNotFound("Family not found"));
        User getter = userRepository.findById(taskRequest.getTaskGetter())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        taskCheck(taskRequest, family, setter, getter);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        //Только тот кто дал задание может менять
        if (!task.getTaskSetter().getId().equals(setter.getId())) {
            throw new AccessDeniedException("Not setter");
        }

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStartDate(taskRequest.getStartDate());
        task.setEndDate(taskRequest.getEndDate());
        task.setRecurring(taskRequest.isRecurring());
        task.setRecurrenceInterval(taskRequest.getRecurrenceInterval());
        task.setTaskGetter(getter);
        task.setRewardPoint(taskRequest.getRewardPoint());

        taskRepository.save(task);
        return toResponse(task);
    }

    public TaskResponse getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepository.findById(Objects.requireNonNull(AuthUtils.getCurrentId()))
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!familyRepository.findByHostsContaining(user)
                .orElseThrow(() -> new FamilyNotFound("Family not found"))
                .getMembers()
                .contains(task.getTaskGetter())) {
            throw new AccessDeniedException("Not allowed to get task");
        }

        return toResponse(task);
    }

    @Transactional
    public void deleteTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        if (!task.getTaskSetter().getId().equals(AuthUtils.getCurrentId())) {
            throw new AccessDeniedException("Not allowed to delete task");
        }
        taskRepository.deleteById(taskId);
    }

    public List<TaskResponse> getTasksByTaskSetterId(Long userId) {
        return taskRepository.findByTaskSetterId(userId).stream().map(this::toResponse).toList();
    }

    public List<TaskResponse> getTasksByTaskGetterId(Long userId) {
        return taskRepository.findByTaskGetterId(userId).stream().map(this::toResponse).toList();
    }
}


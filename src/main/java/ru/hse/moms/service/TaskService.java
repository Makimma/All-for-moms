package ru.hse.moms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.moms.entity.Family;
import ru.hse.moms.entity.RecurringTaskInfo;
import ru.hse.moms.entity.Task;
import ru.hse.moms.entity.User;
import ru.hse.moms.periods.Interval;
import ru.hse.moms.exception.FamilyNotFound;
import ru.hse.moms.exception.InvalidDateException;
import ru.hse.moms.exception.TaskNotFoundException;
import ru.hse.moms.exception.UserNotFoundException;
import ru.hse.moms.repository.FamilyRepository;
import ru.hse.moms.repository.RecurringTaskInfoRepository;
import ru.hse.moms.repository.TaskRepository;
import ru.hse.moms.repository.UserRepository;
import ru.hse.moms.request.RecurringTaskRequest;
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
    private final RecurringTaskInfoRepository recurringTaskInfoRepository;

    private void dateCheck(Date startDate, Date endDate) {
        if (new Date().after(endDate)) {
            throw new InvalidDateException("The end date must be in the future.");
        }

        if (endDate.before(startDate)) {
            throw new InvalidDateException("The end date must be after the start date.");
        }
    }

    private void rewardPointCheck(Family family, User setter, int rewardPoint) {
        //Задание за баллы
        if (rewardPoint != 0) {
            if (!family.getHosts().contains(setter)) {
                throw new AccessDeniedException("Not host");
            }
        }
    }

    private void isInFamily(Family family, User user) {
        //Одна семья
        if (!family.getMembers().contains(user)) {
            throw new AccessDeniedException("Access denied");
        }
    }

    @Transactional
    public List<TaskResponse> createTasks(RecurringTaskRequest recurringTaskRequest) {
        User setter = userRepository.findById(Objects.requireNonNull(AuthUtils.getCurrentId()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        Family family = familyRepository.findByMembersContaining(setter)
                .orElseThrow(() -> new FamilyNotFound("Family not found"));

        rewardPointCheck(family, setter, recurringTaskRequest.getRewardPoint());
        dateCheck(recurringTaskRequest.getStartDate(), recurringTaskRequest.getEndDate());

        List<User> getters = new ArrayList<>();
        for (Long userId : recurringTaskRequest.getTaskGetters()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            getters.add(user);

            isInFamily(family, user);
        }

        RecurringTaskInfo recurringTaskInfo = new RecurringTaskInfo();
        recurringTaskInfo.setSetter(setter);
        recurringTaskInfo.setGetters(getters);
        recurringTaskInfo.setIntervals(recurringTaskRequest.getIntervals());
        recurringTaskInfo.setUserOrder(recurringTaskRequest.getGetterOrder());
        recurringTaskInfo.setStartDate(recurringTaskRequest.getStartDate());
        recurringTaskInfo.setEndDate(recurringTaskRequest.getEndDate());

        recurringTaskInfo = recurringTaskInfoRepository.save(recurringTaskInfo);

        return createTasksFromRecurringInfo(
                recurringTaskInfo,
                recurringTaskRequest.getTitle(),
                recurringTaskRequest.getDescription(),
                recurringTaskRequest.getRewardPoint(),
                recurringTaskRequest.getTaskDuration());
    }

    private List<TaskResponse> createTasksFromRecurringInfo(
            RecurringTaskInfo info,
            String title,
            String description,
            int rewardPoint,
            int taskDuration) {
        List<User> getters = info.getGetters();
        List<Interval> intervals = info.getIntervals();
        List<Integer> userOrder = info.getUserOrder();

        int userIndex = 0;
        int intervalIndex = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(info.getStartDate());

        List<TaskResponse> tasks = new ArrayList<>();
        while (calendar.getTime().before(info.getEndDate())) {
            Interval interval = intervals.get(intervalIndex % intervals.size());
            Task task = Task.builder()
                    .title(title)
                    .description(description)
                    .startDate(calendar.getTime())
                    .endDate(addInterval(calendar.getTime(), taskDuration))
                    .rewardPoint(rewardPoint)
                    .isCompleted(false)
                    .taskSetter(info.getSetter())
                    .taskGetter(getters.get(userOrder.get(userIndex)))
                    .recurringTaskInfo(info)
                    .build();

            tasks.add(toResponse(taskRepository.save(task)));

            userIndex = (userIndex + 1) % userOrder.size();
            intervalIndex++;
            calendar.setTime(addInterval(calendar.getTime(), taskDuration + interval.getDuration()));
        }
        return tasks;
    }

    private Date addInterval(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }


    @Transactional
    public void completeTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

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
                .isCompleted(task.isCompleted())
                .recurringTaskInfoId(task.getRecurringTaskInfo().getId())
                .build();
    }

//    @Transactional
//    public void createRecurringTask(Task task) {
//        Task newTask = new Task();
//        newTask.setDescription(task.getDescription());
//        newTask.setTitle(task.getTitle());
//        newTask.setTaskGetter(task.getTaskGetter());
//
//        Calendar calendarStart = Calendar.getInstance();
//        calendarStart.setTime(task.getStartDate());
//
//        Calendar calendarEnd = Calendar.getInstance();
//        calendarEnd.setTime(task.getEndDate());
//
//        switch (task.getRecurrenceInterval()) {
//            case DAILY:
//                calendarStart.add(Calendar.DAY_OF_YEAR, 1);
//                calendarEnd.add(Calendar.DAY_OF_YEAR, 1);
//                break;
//            case WEEKLY:
//                calendarStart.add(Calendar.WEEK_OF_YEAR, 1);
//                calendarEnd.add(Calendar.WEEK_OF_YEAR, 1);
//                break;
//            case MONTHLY:
//                calendarStart.add(Calendar.MONTH, 1);
//                calendarEnd.add(Calendar.MONTH, 1);
//                break;
//            case YEARLY:
//                calendarStart.add(Calendar.YEAR, 1);
//                calendarEnd.add(Calendar.YEAR, 1);
//                break;
//        }
//
//        task.setCompleted(true);
//        taskRepository.save(task);
//
//        newTask.setStartDate(calendarStart.getTime());
//        newTask.setEndDate(calendarEnd.getTime());
//        taskRepository.save(newTask);
//    }
//

    @Transactional
    public TaskResponse updateTask(Long taskId, TaskRequest taskRequest) {
        User setter = userRepository.findById(Objects.requireNonNull(AuthUtils.getCurrentId()))
                .orElseThrow(() -> new RuntimeException("User not found"));
        Family family = familyRepository.findByMembersContaining(setter)
                .orElseThrow(() -> new FamilyNotFound("Family not found"));
        User getter = userRepository.findById(taskRequest.getTaskGetter())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        isInFamily(family, getter);
        rewardPointCheck(family, setter, taskRequest.getRewardPoint());
        dateCheck(taskRequest.getStartDate(), taskRequest.getEndDate());

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

    @Transactional
    public void deleteRecurringTasks(Long recurringTaskInfoId) {
        RecurringTaskInfo recurringTaskInfo = recurringTaskInfoRepository.findById(recurringTaskInfoId)
                .orElseThrow(() -> new RuntimeException("Recurring task info not found"));
        List<Task> tasks = taskRepository.findByTaskSetterId(recurringTaskInfo.getSetter().getId());


        Date now = new Date();
        for (Task task : tasks) {
            if (task.getStartDate().after(now) && !task.isCompleted()) {
                taskRepository.delete(task);
            }
        }

        recurringTaskInfo.setDeleted(true);
        recurringTaskInfoRepository.save(recurringTaskInfo);
    }

    public List<TaskResponse> getTasksByTaskSetterId(Long userId) {
        return taskRepository.findByTaskSetterId(userId).stream().map(this::toResponse).toList();
    }

    public List<TaskResponse> getTasksByTaskGetterId(Long userId) {
        return taskRepository.findByTaskGetterId(userId).stream().map(this::toResponse).toList();
    }
}


package ru.hse.moms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.hse.moms.service.TaskService;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    private final TaskService taskService;

    @Autowired
    public SchedulerConfig(TaskService taskService) {
        this.taskService = taskService;
    }

//        @Scheduled(fixedRate = 10000)
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleRecurringTasks() {
        taskService.handleRecurringTasks();
    }
}
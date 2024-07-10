package ru.hse.moms.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.hse.moms.entity.Task;

import java.util.Date;

@Data
public class TaskRequest {
    private String title;

    private String description;

    @JsonProperty("start_date")
    private Date startDate;

    @JsonProperty("end_date")
    private Date endDate;

    @JsonProperty("is_recurring")
    private boolean isRecurring;

    @JsonProperty("recurrence_interval")
    private Task.RecurrenceInterval recurrenceInterval;

    @JsonProperty("task_getter")
    private Long taskGetter;

    @JsonProperty("reward_point")
    private int rewardPoint;
}

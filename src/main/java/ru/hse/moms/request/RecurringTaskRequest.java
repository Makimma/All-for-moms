package ru.hse.moms.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.hse.moms.periods.Interval;

import java.util.Date;
import java.util.List;

@Data
public class RecurringTaskRequest {
    private String title;

    private String description;

    @JsonProperty("intervals")
    private List<Interval> intervals;

    @JsonProperty("task_getters")
    private List<Long> taskGetters;

    @JsonProperty("getter_order")
    private List<Integer> getterOrder;

    @JsonProperty("start_date")
    private Date startDate;

    @JsonProperty("end_date")
    private Date endDate;

    @JsonProperty("reward_point")
    private int rewardPoint;

    @JsonProperty("task_duration")
    private int taskDuration;
}

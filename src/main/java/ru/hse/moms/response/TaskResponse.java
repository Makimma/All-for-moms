package ru.hse.moms.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;

    @JsonProperty("start_date")
    private Date startDate;

    @JsonProperty("end_date")
    private Date endDate;

    @JsonProperty("task_getter")
    private Long taskGetter;

    @JsonProperty("reward_point")
    private int rewardPoint;

    @JsonProperty("is_completed")
    private boolean isCompleted;

    @JsonProperty("recurring_task_info_id")
    private Long recurringTaskInfoId;
}

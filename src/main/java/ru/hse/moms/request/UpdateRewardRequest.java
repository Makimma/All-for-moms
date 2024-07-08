package ru.hse.moms.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateRewardRequest {
    @JsonProperty("reward_id")
    private Long rewardId;

    private Integer cost;
    private String description;

    @JsonProperty("short_name")
    private String shortName;

    private Integer quantity;
}

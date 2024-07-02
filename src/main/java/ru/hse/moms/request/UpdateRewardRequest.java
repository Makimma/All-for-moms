package ru.hse.moms.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateRewardRequest {
    private Long rewardId;
    private Integer cost;
    private String description;
}

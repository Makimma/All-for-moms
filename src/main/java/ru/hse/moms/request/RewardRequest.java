package ru.hse.moms.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RewardRequest {
    private String description;
    private int cost;
}

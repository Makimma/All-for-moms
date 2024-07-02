package ru.hse.moms.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RewardResponse {
    private int cost;
    private String description;
}

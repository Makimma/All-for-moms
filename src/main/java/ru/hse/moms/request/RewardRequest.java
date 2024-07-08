package ru.hse.moms.request;

import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
public class RewardRequest {
    private String description;
    private int cost;
    private int quantity;

    @JsonProperty("short_name")
    private String shortName;
}

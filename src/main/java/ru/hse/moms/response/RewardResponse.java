package ru.hse.moms.response;

import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
public class RewardResponse {
    private Long id;
    private int cost;
    private String description;
    private int quantity;

    @JsonProperty("short_name")
    private String shortName;
}

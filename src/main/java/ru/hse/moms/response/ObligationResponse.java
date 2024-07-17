package ru.hse.moms.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.hse.moms.entity.User;

@Data
@Builder
public class ObligationResponse {
    private Long id;
    @JsonProperty("from_user")
    private UserResponse fromUser;

    @JsonProperty("to_user")
    private UserResponse toUser;

    private String description;

    private Double money;

    @JsonProperty("status_of_obligation")
    private boolean statusOfObligation;

}

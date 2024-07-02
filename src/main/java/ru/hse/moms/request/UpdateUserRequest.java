package ru.hse.moms.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class UpdateUserRequest {
//    @JsonProperty("user_id")
//    @NotNull
//    private Long userId;
    private String email;
    private String password;
}

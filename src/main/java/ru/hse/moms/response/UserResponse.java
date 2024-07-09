package ru.hse.moms.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.hse.moms.entity.Reward;
import ru.hse.moms.entity.Role;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    private TypeResponse type;
    private DiaryResponse diary;
    private List<RewardResponse> rewards;
}

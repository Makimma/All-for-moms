package ru.hse.moms.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.hse.moms.entity.Role;

import java.util.Date;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    private TypeResponse type;
}

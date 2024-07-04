package ru.hse.moms.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SignUpRequest {
    @NotNull
    private String email;
    @NotNull
    private String username;
    @NotNull
    private String name;
    @NotNull
    private String password;

    @NotNull
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
}

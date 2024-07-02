package ru.hse.moms.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
}

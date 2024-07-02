package ru.hse.moms.request;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class GetUserRequest {
    private Long id;
    private String username;
    private String email;
}

package ru.hse.moms.response;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FamilyResponse {
    List<UserResponse> members;
    UserResponse host;
}

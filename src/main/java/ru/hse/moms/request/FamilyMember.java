package ru.hse.moms.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FamilyMember {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("member_type_id")
    private Long typeId;
}

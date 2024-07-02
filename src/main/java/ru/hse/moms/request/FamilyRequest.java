package ru.hse.moms.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FamilyRequest {
    @JsonProperty("members_id")
    List<Long> membersId;

    @JsonProperty("host_id")
    Long hostId;
}

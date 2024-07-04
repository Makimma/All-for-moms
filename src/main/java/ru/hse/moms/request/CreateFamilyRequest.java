package ru.hse.moms.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateFamilyRequest {
    @JsonProperty("members_id")
    List<Long> membersId;
}

package ru.hse.moms.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdateFamilyRequest {
    @JsonProperty("members_id")
    List<Long> membersId;

    @JsonProperty("hosts_id")
    List<Long> hostsId;
}

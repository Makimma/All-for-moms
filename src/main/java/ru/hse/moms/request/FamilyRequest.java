package ru.hse.moms.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.hse.moms.entity.Type;

import java.util.List;

@Data
@Builder
public class FamilyRequest {
    @JsonProperty("members")
    List<Member> members;

    @JsonProperty("host_id")
    Long hostId;

    @Data
    @Builder
    public static class Member {
        @JsonProperty("member_id")
        Long memberId;

        @JsonProperty("member_type_id")
        Long typeId;
    }
}

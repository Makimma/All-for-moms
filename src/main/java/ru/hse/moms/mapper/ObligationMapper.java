package ru.hse.moms.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.moms.entity.Obligation;
import ru.hse.moms.response.ObligationResponse;

@Component
@RequiredArgsConstructor
public class ObligationMapper {
    private final UserMapper userMapper;
    public ObligationResponse makeObligationResponse(Obligation obligation) {
        return ObligationResponse.builder()
                .id(obligation.getId())
                .fromUser(userMapper.makeUserResponse(obligation.getFromUser()))
                .toUser(userMapper.makeUserResponse(obligation.getToUser()))
                .statusOfObligation(obligation.isTransferred())
                .money(obligation.getMoney())
                .description(obligation.getDescription())
                .build();
    }
}

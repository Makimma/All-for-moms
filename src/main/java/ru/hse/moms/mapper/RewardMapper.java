package ru.hse.moms.mapper;

import org.springframework.stereotype.Component;
import ru.hse.moms.entity.Reward;
import ru.hse.moms.response.RewardResponse;

@Component
public class RewardMapper {
    public RewardResponse makeRewardResponse(Reward reward) {
        return RewardResponse.builder()
                .id(reward.getId())
                .description(reward.getDescription())
                .cost(reward.getCost())
                .quantity(reward.getQuantity())
                .shortName(reward.getShortName())
                .build();
    }
}

package ru.hse.moms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.moms.entity.Reward;
import ru.hse.moms.exception.RewardNotFoundException;
import ru.hse.moms.mapper.RewardMapper;
import ru.hse.moms.repository.RewardRepository;
import ru.hse.moms.request.RewardRequest;
import ru.hse.moms.request.UpdateRewardRequest;
import ru.hse.moms.response.RewardResponse;
import ru.hse.moms.security.AuthUtils;

@RequiredArgsConstructor
@Service
public class RewardService {
    private final RewardRepository rewardRepository;
    private final RewardMapper rewardMapper;
    private final UserService userService;

    public RewardResponse getReward(Long id) {
        return rewardMapper.makeRewardResponse(rewardRepository.findById(id)
                .orElseThrow(() ->
                        new RewardNotFoundException(String.format("Reward with id: %s not found", id))));
    }

    public RewardResponse createReward(RewardRequest rewardRequest) {
        Reward reward = Reward.builder()
                .description(rewardRequest.getDescription())
                .cost(rewardRequest.getCost())
                .quantity(rewardRequest.getQuantity())
                .shortName(rewardRequest.getShortName())
                .build();
        rewardRepository.saveAndFlush(reward);
        userService.addReward(reward.getId());
        return rewardMapper.makeRewardResponse(reward);
    }

    public RewardResponse updateReward(UpdateRewardRequest updateRewardRequest) {
        Reward reward = rewardRepository.findById(updateRewardRequest.getRewardId()).
                orElseThrow(() ->new RewardNotFoundException(
                        String.format("Reward with id: %s not found",updateRewardRequest.getRewardId())));
        if (updateRewardRequest.getCost() != null) {
            reward.setCost(updateRewardRequest.getCost());
        }
        if (updateRewardRequest.getDescription() != null) {
            reward.setDescription(updateRewardRequest.getDescription());
        }
        if (updateRewardRequest.getQuantity() != null) {
            reward.setQuantity(updateRewardRequest.getQuantity());
        }
        if (updateRewardRequest.getShortName() != null) {
            reward.setShortName(updateRewardRequest.getShortName());
        }
        return rewardMapper.makeRewardResponse(rewardRepository.saveAndFlush(reward));
    }
    public boolean deleteReward(Long rewardId) {
        if (!rewardRepository.existsById(rewardId)) {
            throw new RewardNotFoundException(String.format("Reward with id: %s not found",rewardId));
        }
        rewardRepository.deleteById(rewardId);
        return true;
    }
}

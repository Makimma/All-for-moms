package ru.hse.moms.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.moms.request.RewardRequest;
import ru.hse.moms.request.UpdateRewardRequest;
import ru.hse.moms.service.RewardService;

@RestController
@RequestMapping("/api/reward")
@RequiredArgsConstructor
public class RewardController {
    private final RewardService rewardService;
    @GetMapping()
    public ResponseEntity<?> getReward(@RequestParam("reward_id") Long rewardId) {
        return ResponseEntity.ok(rewardService.getReward(rewardId));
    }

    @PutMapping()
    public ResponseEntity<?> updateReward(@RequestBody UpdateRewardRequest rewardRequest){
        return ResponseEntity.ok(rewardService.updateReward(rewardRequest));
    }
    @DeleteMapping()
    public ResponseEntity<?> deleteReward(@RequestParam("reward_id") Long rewardId) {
        return ResponseEntity.ok(rewardService.deleteReward(rewardId));
    }
    @PostMapping()
    public ResponseEntity<?> createReward(@RequestBody RewardRequest rewardRequest) {
        return ResponseEntity.ok(rewardService.createReward(rewardRequest));
    }
    @GetMapping("/get-all-rewards")
    public ResponseEntity<?> getAllRewardsForUser(@RequestParam(name = "user_id") Long userId) {
        return ResponseEntity.ok(rewardService.getAllRewardsForUser(userId));
    }
}

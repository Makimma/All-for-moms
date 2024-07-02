package ru.hse.moms.controller;

import lombok.RequiredArgsConstructor;
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
    @GetMapping("/get-reward")
    public ResponseEntity<?> getReward(@RequestParam("reward_id") Long rewardId) {
        return ResponseEntity.ok(rewardService.getReward(rewardId));
    }

    @PutMapping("/update-reward")
    public ResponseEntity<?> updateReward(@RequestBody UpdateRewardRequest rewardRequest){
        return ResponseEntity.ok(rewardService.updateReward(rewardRequest));
    }
    @DeleteMapping("/delete-reward")
    public ResponseEntity<?> deleteReward(@RequestParam("reward_id") Long rewardId) {
        return ResponseEntity.ok(rewardService.deleteReward(rewardId));
    }
    @PostMapping("/create-reward")
    public ResponseEntity<?> createReward(@RequestBody RewardRequest rewardRequest) {
        return ResponseEntity.ok(rewardService.createReward(rewardRequest));
    }
}

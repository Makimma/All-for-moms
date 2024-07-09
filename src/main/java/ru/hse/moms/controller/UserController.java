package ru.hse.moms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.moms.request.GetUserRequest;
import ru.hse.moms.request.UpdateUserRequest;
import ru.hse.moms.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<?> getUser(@RequestBody GetUserRequest getUserRequest) throws Exception {
        return ResponseEntity.ok(userService.getUser(getUserRequest));
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest updateUserRequest){
        return ResponseEntity.ok(userService.updateUser(updateUserRequest));
    }

    @GetMapping("/get-current-user")
    public ResponseEntity<?> getCurrentUser(){
        return ResponseEntity.ok(userService.getCurrentUser());
    }


    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestParam("user_id") Long userId) {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }
    @PutMapping("/add-reward")
    public ResponseEntity<?> addRewardToCurrentUser(@RequestParam("reward_id") Long rewardId) {
        return ResponseEntity.ok(userService.addReward(rewardId));
    }
    @PutMapping("/get-reward")
    public ResponseEntity<?> getRewardFromOtherUser(@RequestParam("reward_id") Long rewardId) {
        return ResponseEntity.ok(userService.getReward(rewardId));
    }

}

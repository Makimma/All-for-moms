package ru.hse.moms.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.moms.entity.User;
import ru.hse.moms.response.UserResponse;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final TypeMapper typeMapper;
    private final DiaryMapper diaryMapper;
    private final RewardMapper rewardMapper;
    public UserResponse makeUserResponse(User user) {
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .username(user.getUsername())
                .diary(diaryMapper.makeDiaryResponse(user.getDiary()))
                .build();
        if (user.getType() != null) {
            userResponse.setType(typeMapper.makeTypeResponse(user.getType()));
        }
        if (user.getRewards() != null) {
            userResponse.setRewards(user.getRewards().stream().map(rewardMapper::makeRewardResponse).toList());
        }
        return userResponse;
    }
}

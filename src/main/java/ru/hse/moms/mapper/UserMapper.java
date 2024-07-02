package ru.hse.moms.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.moms.entity.User;
import ru.hse.moms.response.UserResponse;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final TypeMapper typeMapper;
    public UserResponse makeUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .username(user.getUsername())
                .type(typeMapper.makeTypeResponse(user.getType()))
                .build();
    }
}

package ru.hse.moms.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.moms.entity.Family;
import ru.hse.moms.response.FamilyResponse;

@Component
@RequiredArgsConstructor
public class FamilyMapper {
    private final UserMapper userMapper;
    public FamilyResponse makeFamilyResponse(Family family) {
        return FamilyResponse.builder()
                .members(
                        family.getMembers().stream().map(userMapper::makeUserResponse).toList()
                )
                .hosts(family.getHosts().stream().map(userMapper::makeUserResponse).toList())
                .build();
    }
}

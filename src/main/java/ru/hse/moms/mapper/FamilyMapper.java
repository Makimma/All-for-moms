package ru.hse.moms.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.moms.entity.Family;
import ru.hse.moms.repository.FamilyRepository;
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
                .host(userMapper.makeUserResponse(family.getHost()))
                .build();
    }
}

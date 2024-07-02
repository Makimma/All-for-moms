package ru.hse.moms.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.moms.entity.Type;
import ru.hse.moms.response.TypeResponse;

@Component
@RequiredArgsConstructor
public class TypeMapper {
    public TypeResponse makeTypeResponse(Type type) {
        return TypeResponse.builder()
                .type(type.getTypeName())
                .build();
    }
}

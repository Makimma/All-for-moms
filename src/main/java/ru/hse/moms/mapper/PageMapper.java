package ru.hse.moms.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.moms.entity.Page;
import ru.hse.moms.response.PageResponse;

@Component
@RequiredArgsConstructor
public class PageMapper {
    public PageResponse makePageResponse(Page page) {
        return PageResponse.builder()
                .id(page.getId())
                .title(page.getTitle())
                .content(page.getContent())
                .date(page.getDate())
                .build();
    }
}

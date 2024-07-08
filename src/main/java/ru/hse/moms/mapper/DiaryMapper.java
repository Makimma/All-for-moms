package ru.hse.moms.mapper;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.moms.entity.Diary;
import ru.hse.moms.response.DiaryResponse;

@RequiredArgsConstructor
@Component
public class DiaryMapper {
    private final PageMapper pageMapper;
    public DiaryResponse makeDiaryResponse(Diary diary) {
        return DiaryResponse.builder()
                .id(diary.getId())
                .pages(diary.getPages().stream().map(pageMapper::makePageResponse).toList())
                .build();
    }
}

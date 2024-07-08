package ru.hse.moms.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.moms.entity.User;
import ru.hse.moms.mapper.DiaryMapper;
import ru.hse.moms.response.DiaryResponse;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryMapper diaryMapper;
    private final FamilyService familyService;
    public DiaryResponse getDiary() {
        User user = familyService.getCurrentUser();
        return diaryMapper.makeDiaryResponse(user.getDiary());
    }
    
}

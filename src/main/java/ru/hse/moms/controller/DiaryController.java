package ru.hse.moms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.moms.response.DiaryResponse;
import ru.hse.moms.service.DiaryService;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;
    @GetMapping
    public DiaryResponse getDiary() {
        return diaryService.getDiary();
    }
}

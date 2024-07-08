package ru.hse.moms.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DiaryResponse {
    private Long id;
    private List<PageResponse> pages;
}

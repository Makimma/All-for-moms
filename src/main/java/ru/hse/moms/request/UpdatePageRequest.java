package ru.hse.moms.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatePageRequest {
    private Long pageId;
    private String title;
    private String content;
}

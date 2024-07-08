package ru.hse.moms.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePageRequest {
    private String title;
    private String content;
}
package ru.hse.moms.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PageResponse {
    private Long id;
    private String title;
    private String content;
    private Date date;
}

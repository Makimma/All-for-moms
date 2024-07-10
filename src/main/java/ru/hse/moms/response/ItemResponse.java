package ru.hse.moms.response;

import lombok.Data;

@Data
public class ItemResponse {
    private Long id;
    private String name;
    private boolean allowAlternatives;
    private boolean isReserved;
    private String link;
}

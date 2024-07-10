package ru.hse.moms.request;

import lombok.Data;

@Data
public class ItemRequest {
    private String name;
    private boolean allowAlternatives;
    private String link;
}
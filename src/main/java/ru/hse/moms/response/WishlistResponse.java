package ru.hse.moms.response;

import lombok.Data;

import java.util.List;

@Data
public class WishlistResponse {
    private Long id;
    private List<ItemResponse> items;
}

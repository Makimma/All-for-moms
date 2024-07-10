package ru.hse.moms.request;

import lombok.Data;

import java.util.List;

@Data
public class WishlistRequest {
    private List<ItemRequest> items;
}
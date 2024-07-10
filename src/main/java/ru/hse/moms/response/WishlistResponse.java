package ru.hse.moms.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WishlistResponse {
    private Long id;
    private List<ItemResponse> items;
}

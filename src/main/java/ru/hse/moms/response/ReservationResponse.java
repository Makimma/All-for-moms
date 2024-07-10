package ru.hse.moms.response;

import lombok.Data;

import java.util.List;

@Data
public class ReservationResponse {
    private List<ItemResponse> items;
}

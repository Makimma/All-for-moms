package ru.hse.moms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.moms.response.ReservationResponse;
import ru.hse.moms.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/item/{itemId}")
    public ResponseEntity<ReservationResponse> reserveItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(reservationService.reserveItem(itemId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReservationResponse>> getMyReservations() {
        return ResponseEntity.ok(reservationService.getUserReservations());
    }
}
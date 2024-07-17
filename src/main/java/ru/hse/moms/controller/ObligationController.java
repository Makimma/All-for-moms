package ru.hse.moms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.moms.response.ObligationResponse;
import ru.hse.moms.service.ObligationService;

@RestController
@RequiredArgsConstructor
public class ObligationController {
    private final ObligationService obligationService;

    public ResponseEntity<?> getObligation(@RequestParam("obligation_id") Long obligationId) {
        return ResponseEntity.ok(obligationService.getObligation(obligationId));
    }
    public ResponseEntity<?> changeStateOfObligation(@RequestParam("obligation_id") Long obligationId,
                                                     @RequestParam("state") boolean state) {
        return ResponseEntity.ok(obligationService.changeStateOfObligation(obligationId, state));
    }
    // who should return money to user
    public ResponseEntity<?> getAllDebts() {
        return ResponseEntity.ok(obligationService.getAllDebts());
    }
    // to who user should return money
    public ResponseEntity<?> getAllCreditors() {
        return ResponseEntity.ok(obligationService.getAllCreditors());
    }
}

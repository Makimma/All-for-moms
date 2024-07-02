package ru.hse.moms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.moms.request.FamilyRequest;
import ru.hse.moms.request.GetUserRequest;
import ru.hse.moms.request.UpdateUserRequest;
import ru.hse.moms.service.FamilyService;

@RestController
@RequestMapping("/api/family")
@RequiredArgsConstructor
public class FamilyController {
    private final FamilyService familyService;
    @GetMapping("/get-family")
    public ResponseEntity<?> getFamily(@RequestParam("family_id") Long familyId) {
        return ResponseEntity.ok(familyService.getFamily(familyId));
    }

    @PutMapping("/update-family")
    public ResponseEntity<?> updateFamily(@RequestBody FamilyRequest familyRequest){
        return ResponseEntity.ok(familyService.updateFamily(familyRequest));
    }
    @DeleteMapping("/delete-family")
    public ResponseEntity<?> deletefamily(@RequestParam("family_id") Long familyId) {
        return ResponseEntity.ok(familyService.deleteFamily());
    }
    @PostMapping("/create-family")
    public ResponseEntity<?> createFamily(@RequestBody FamilyRequest familyRequest) {
        return ResponseEntity.ok(familyService.createFamily(familyRequest));
    }
}

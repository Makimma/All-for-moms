package ru.hse.moms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.moms.request.CreateFamilyRequest;
import ru.hse.moms.request.UpdateFamilyRequest;
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
    public ResponseEntity<?> updateFamily(@RequestBody UpdateFamilyRequest updateFamilyRequest){
        return ResponseEntity.ok(familyService.addFamilyMemberHost(updateFamilyRequest));
    }
    @DeleteMapping("/delete-family")
    public ResponseEntity<?> deleteFamily(@RequestParam("family_id") Long familyId) {
        return ResponseEntity.ok(familyService.deleteFamily());
    }
    @PostMapping("/create-family")
    public ResponseEntity<?> createFamily(@RequestBody CreateFamilyRequest createFamilyRequest) {
        return ResponseEntity.ok(familyService.createFamily(createFamilyRequest));
    }
}

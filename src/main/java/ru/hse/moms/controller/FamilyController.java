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
    @GetMapping()
    public ResponseEntity<?> getFamily(@RequestParam("family_id") Long familyId) {
        return ResponseEntity.ok(familyService.getFamily(familyId));
    }

    @PutMapping()
    public ResponseEntity<?> addFamilyMemberOrHost(@RequestBody UpdateFamilyRequest updateFamilyRequest){
        return ResponseEntity.ok(familyService.addFamilyMemberOrHost(updateFamilyRequest));
    }
    @DeleteMapping()
    public ResponseEntity<?> deleteFamily() {
        return ResponseEntity.ok(familyService.deleteFamily());
    }
    @PostMapping()
    public ResponseEntity<?> createFamily(@RequestBody CreateFamilyRequest createFamilyRequest) {
        return ResponseEntity.ok(familyService.createFamily(createFamilyRequest));
    }
    @DeleteMapping("/delete-family-member-hosts")
    public ResponseEntity<?> deleteFamilyMembersOrHosts(@RequestBody UpdateFamilyRequest updateFamilyRequest) {
        return ResponseEntity.ok(familyService.deleteFamilyMembersOrHosts(updateFamilyRequest));
    }
}

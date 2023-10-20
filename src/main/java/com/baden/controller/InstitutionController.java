package com.baden.controller;

import com.baden.customResponse.SuccessResponse;
import com.baden.entity.InstitutionEntity;
import com.baden.service.InstitutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    // Create a new institution
    @PostMapping("/institution")
    public ResponseEntity<InstitutionEntity> save(@RequestBody InstitutionEntity institutionEntity) {
        return ResponseEntity.ok().body(institutionService.saveInstitution(institutionEntity));
    }

    // Get all institutions
    @GetMapping("/institutions")
    public ResponseEntity<List<InstitutionEntity>> getAll() {
        return ResponseEntity.ok().body(institutionService.getAllInstitutions());
    }

    // Get institution by id
    @GetMapping("/institution/{institution_id}")
    public ResponseEntity<InstitutionEntity> getById(@PathVariable Integer institution_id) {
        return ResponseEntity.ok().body(institutionService.getInstitutionById(institution_id));
    }

    // Update institution details by idgi
    @PutMapping("/institution/{institution_id}")
    public ResponseEntity<InstitutionEntity> update(@PathVariable Integer institution_id, @RequestBody InstitutionEntity institutionEntity) {
        return ResponseEntity.ok().body(institutionService.updateInstitution(institution_id, institutionEntity));

    }

    //Delete institution by id
    @DeleteMapping("/institution/{institution_id}")
    public ResponseEntity<SuccessResponse> delete(@PathVariable Integer institution_id) {
        return ResponseEntity.ok().body(institutionService.deleteInstitution(institution_id));
    }

}

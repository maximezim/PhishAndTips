package com.learnandphish.formation.controller;

import com.learnandphish.formation.dto.FormationRequest;
import com.learnandphish.formation.model.Formation;
import com.learnandphish.formation.service.FormationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/formation")
@RequiredArgsConstructor
public class FormationController {
    private final FormationService formationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Formation> createFormation(@RequestBody FormationRequest formationRequest) {
        Formation createdFormation = formationService.createFormation(formationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFormation);

    }

    @GetMapping
    public ResponseEntity<List<Formation>> getAllFormations() {
        List<Formation> formations = formationService.getAllFormations();
        return ResponseEntity.ok(formations);
    }

    @GetMapping("/{formationId}")
    public ResponseEntity<Formation> getFormationById(@PathVariable Long formationId) {
        Formation formation = formationService.getFormationById(formationId);
        return formation != null ? ResponseEntity.ok(formation) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{formationId}")
    public ResponseEntity<Void> deleteFormation(@PathVariable Long formationId) {
        formationService.deleteFormation(formationId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Formation> updateFormation(@RequestBody FormationRequest formationRequest) {
        Formation updatedFormation = formationService.updateFormation(formationRequest);
        return ResponseEntity.ok(updatedFormation);
    }


}

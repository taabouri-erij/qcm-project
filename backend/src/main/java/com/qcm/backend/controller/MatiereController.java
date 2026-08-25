package com.qcm.backend.controller;

import com.qcm.backend.dto.MatiereDTO;
import com.qcm.backend.entity.Matiere;
import com.qcm.backend.service.MatiereService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matieres")
@CrossOrigin(origins = "*")
public class MatiereController {

    private final MatiereService matiereService;

    public MatiereController(MatiereService matiereService) {
        this.matiereService = matiereService;
    }

    @GetMapping
    public List<MatiereDTO> getAllMatieres() {
        return matiereService.getAllMatieres().stream().map(matiereService::convertToDTO).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatiereDTO> getMatiereById(@PathVariable Long id) {
        return matiereService.getMatiereById(id)
                .map(matiereService::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/module/{moduleId}")
    public List<MatiereDTO> getMatieresByModule(@PathVariable Long moduleId) {
        return matiereService.getMatieresByModule(moduleId).stream().map(matiereService::convertToDTO).toList();
    }

    @PostMapping
    public MatiereDTO createMatiere(@RequestBody Matiere matiere) {
        return matiereService.convertToDTO(matiereService.createMatiere(matiere));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatiereDTO> updateMatiere(@PathVariable Long id, @RequestBody Matiere details) {
        try {
            return ResponseEntity.ok(matiereService.convertToDTO(matiereService.updateMatiere(id, details)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatiere(@PathVariable Long id) {
        matiereService.deleteMatiere(id);
        return ResponseEntity.noContent().build();
    }
}
package com.qcm.backend.controller;

import com.qcm.backend.entity.Chapitre;
import com.qcm.backend.service.ChapitreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapitres")
@CrossOrigin(origins = "*")
public class ChapitreController {

    private final ChapitreService chapitreService;

    public ChapitreController(ChapitreService chapitreService) {
        this.chapitreService = chapitreService;
    }

    @GetMapping
    public List<Chapitre> getAllChapitres() {
        return chapitreService.getAllChapitres();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chapitre> getChapitreById(@PathVariable Long id) {
        return chapitreService.getChapitreById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/matiere/{matiereId}")
    public List<Chapitre> getChapitresByMatiere(@PathVariable Long matiereId) {
        return chapitreService.getChapitresByMatiere(matiereId);
    }

    @PostMapping
    public Chapitre createChapitre(@RequestBody Chapitre chapitre) {
        return chapitreService.createChapitre(chapitre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chapitre> updateChapitre(@PathVariable Long id, @RequestBody Chapitre details) {
        try {
            return ResponseEntity.ok(chapitreService.updateChapitre(id, details));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapitre(@PathVariable Long id) {
        chapitreService.deleteChapitre(id);
        return ResponseEntity.noContent().build();
    }
}
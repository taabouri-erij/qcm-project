package com.qcm.backend.controller;

import com.qcm.backend.entity.EnseignantMatiere;
import com.qcm.backend.service.EnseignantMatiereService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enseignant-matieres")
@CrossOrigin(origins = "*")
public class EnseignantMatiereController {

    private final EnseignantMatiereService service;

    public EnseignantMatiereController(EnseignantMatiereService service) {
        this.service = service;
    }

    @GetMapping("/enseignant/{enseignantId}")
    public List<EnseignantMatiere> getByEnseignant(@PathVariable Long enseignantId) {
        return service.getByEnseignant(enseignantId);
    }

    @GetMapping("/matiere/{matiereId}")
    public List<EnseignantMatiere> getByMatiere(@PathVariable Long matiereId) {
        return service.getByMatiere(matiereId);
    }

    // Body : { "enseignantId": 1, "matiereId": 2 }
    @PostMapping
    public EnseignantMatiere associer(@RequestBody Map<String, Long> body) {
        return service.associer(body.get("enseignantId"), body.get("matiereId"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> dissocier(@PathVariable Long id) {
        service.dissocier(id);
        return ResponseEntity.noContent().build();
    }
}
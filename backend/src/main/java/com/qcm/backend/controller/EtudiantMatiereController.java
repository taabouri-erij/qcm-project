package com.qcm.backend.controller;

import com.qcm.backend.dto.EtudiantMatiereDTO;
import com.qcm.backend.service.EtudiantMatiereService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/etudiant-matieres")
@CrossOrigin(origins = "*")
public class EtudiantMatiereController {

    private final EtudiantMatiereService service;

    public EtudiantMatiereController(EtudiantMatiereService service) {
        this.service = service;
    }

    @GetMapping("/etudiant/{etudiantId}")
    public List<EtudiantMatiereDTO> getByEtudiant(@PathVariable Long etudiantId) {
        return service.getByEtudiant(etudiantId).stream().map(service::convertToDTO).toList();
    }

    @GetMapping("/matiere/{matiereId}")
    public List<EtudiantMatiereDTO> getByMatiere(@PathVariable Long matiereId) {
        return service.getByMatiere(matiereId).stream().map(service::convertToDTO).toList();
    }

    @PostMapping
    public EtudiantMatiereDTO associer(@RequestBody Map<String, Long> body) {
        return service.convertToDTO(service.associer(body.get("etudiantId"), body.get("matiereId")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> dissocier(@PathVariable Long id) {
        service.dissocier(id);
        return ResponseEntity.noContent().build();
    }
}
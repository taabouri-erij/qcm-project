package com.qcm.backend.controller;

import com.qcm.backend.dto.ReponseEtudiantDTO;
import com.qcm.backend.entity.ReponseEtudiant;
import com.qcm.backend.service.ReponseEtudiantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reponses-etudiant")
@CrossOrigin(origins = "*")
public class ReponseEtudiantController {

    private final ReponseEtudiantService service;

    public ReponseEtudiantController(ReponseEtudiantService service) {
        this.service = service;
    }

    @GetMapping("/tentative/{tentativeId}")
    public List<ReponseEtudiantDTO> getByTentative(@PathVariable Long tentativeId) {
        return service.getByTentative(tentativeId).stream().map(service::convertToDTO).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReponseEtudiantDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(service::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ReponseEtudiantDTO create(@RequestBody ReponseEtudiant reponse) {
        return service.convertToDTO(service.create(reponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReponseEtudiantDTO> update(@PathVariable Long id, @RequestBody ReponseEtudiant details) {
        try {
            return ResponseEntity.ok(service.convertToDTO(service.update(id, details)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
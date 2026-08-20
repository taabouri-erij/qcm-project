package com.qcm.backend.controller;

import com.qcm.backend.entity.Evaluation;
import com.qcm.backend.entity.Matiere;
import com.qcm.backend.service.EtudiantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etudiant")
@CrossOrigin(origins = "*")
public class EtudiantController {

    private final EtudiantService etudiantService;

    public EtudiantController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    // Matières de l'étudiant
    @GetMapping("/{etudiantId}/matieres")
    public List<Matiere> getMatieres(@PathVariable Long etudiantId) {
        return etudiantService.getMatieres(etudiantId);
    }

    // Évaluations disponibles pour l'étudiant
    @GetMapping("/{etudiantId}/evaluations")
    public List<Evaluation> getEvaluations(@PathVariable Long etudiantId) {
        return etudiantService.getEvaluationsDisponibles(etudiantId);
    }
}
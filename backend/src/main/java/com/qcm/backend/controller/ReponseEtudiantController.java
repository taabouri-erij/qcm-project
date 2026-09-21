package com.qcm.backend.controller;

import com.qcm.backend.dto.ReponseEtudiantDTO;
import com.qcm.backend.entity.ReponseEtudiant;
import com.qcm.backend.entity.Tentative;
import com.qcm.backend.exception.RessourceNonTrouveeException;
import com.qcm.backend.security.AuthUtils;
import com.qcm.backend.service.ReponseEtudiantService;
import com.qcm.backend.service.TentativeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reponses-etudiant")
@CrossOrigin(origins = "*")
public class ReponseEtudiantController {

    private final ReponseEtudiantService service;
    private final TentativeService tentativeService;

    public ReponseEtudiantController(ReponseEtudiantService service, TentativeService tentativeService) {
        this.service = service;
        this.tentativeService = tentativeService;
    }

    @GetMapping("/tentative/{tentativeId}")
    public List<ReponseEtudiantDTO> getByTentative(@PathVariable Long tentativeId) {
        if (!AuthUtils.estEnseignantOuAdmin()) {
            Tentative tentative = tentativeService.getTentativeById(tentativeId)
                    .orElseThrow(() -> new RessourceNonTrouveeException("Tentative non trouvée"));
            if (!tentative.getEtudiant().getId().equals(AuthUtils.getUserIdConnecte())) {
                throw new RessourceNonTrouveeException("Tentative non trouvée");
            }
        }
        return service.getByTentative(tentativeId).stream().map(service::convertToDTO).toList();
    }

    @GetMapping("/{id}")
    public ReponseEtudiantDTO getById(@PathVariable Long id) {
        return service.getById(id)
                .map(service::convertToDTO)
                .orElseThrow(() -> new RessourceNonTrouveeException("Réponse non trouvée"));
    }
}
package com.qcm.backend.controller;

import com.qcm.backend.dto.TentativeDTO;
import com.qcm.backend.entity.Tentative;
import com.qcm.backend.exception.RessourceNonTrouveeException;
import com.qcm.backend.security.AuthUtils;
import com.qcm.backend.service.NotationService;
import com.qcm.backend.service.TentativeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tentatives")
@CrossOrigin(origins = "*")
public class TentativeController {

    private final TentativeService tentativeService;
    private final NotationService notationService;

    public TentativeController(TentativeService tentativeService,
                               NotationService notationService) {
        this.tentativeService = tentativeService;
        this.notationService = notationService;
    }

    // Vérifie que l'utilisateur connecté a le droit de voir/agir sur cette tentative :
    // soit c'est SA PROPRE tentative (étudiant), soit c'est un enseignant/admin
    private void verifierProprietaireTentative(Tentative tentative) {
        if (AuthUtils.estEnseignantOuAdmin()) {
            return; // accès élargi autorisé
        }
        Long userIdConnecte = AuthUtils.getUserIdConnecte();
        if (!tentative.getEtudiant().getId().equals(userIdConnecte)) {
            throw new RessourceNonTrouveeException("Tentative non trouvée");
            // Remarque volontaire : on renvoie "non trouvée" plutôt que "accès refusé".
            // Cela évite de révéler à un attaquant que l'ID existe mais appartient à quelqu'un d'autre.
        }
    }

    // Vérifie que l'utilisateur connecté correspond bien à l'etudiantId demandé
    // (sauf enseignant/admin)
    private void verifierEtudiantConnecte(Long etudiantId) {
        if (AuthUtils.estEnseignantOuAdmin()) {
            return;
        }
        Long userIdConnecte = AuthUtils.getUserIdConnecte();
        if (!etudiantId.equals(userIdConnecte)) {
            throw new RessourceNonTrouveeException("Ressource non trouvée");
        }
    }

    // =========================================================
    // CONSULTATION
    // =========================================================

    @GetMapping("/{id}")
    public TentativeDTO getTentativeById(@PathVariable Long id) {
        Tentative tentative = tentativeService.getTentativeById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Tentative non trouvée"));
        verifierProprietaireTentative(tentative);
        return tentativeService.convertToDTO(tentative);
    }

    @GetMapping("/etudiant/{etudiantId}")
    public List<TentativeDTO> getByEtudiant(@PathVariable Long etudiantId) {
        verifierEtudiantConnecte(etudiantId);
        return tentativeService.getTentativesByEtudiant(etudiantId)
                .stream()
                .map(tentativeService::convertToDTO)
                .toList();
    }

    @GetMapping("/evaluation/{evaluationId}")
    public List<TentativeDTO> getByEvaluation(@PathVariable Long evaluationId) {
        // Réservé aux enseignants/admins : voir TOUTES les tentatives d'une évaluation
        // n'a de sens que pour corriger/superviser, pas pour un étudiant
        if (!AuthUtils.estEnseignantOuAdmin()) {
            throw new RessourceNonTrouveeException("Ressource non trouvée");
        }
        return tentativeService.getTentativesByEvaluation(evaluationId)
                .stream()
                .map(tentativeService::convertToDTO)
                .toList();
    }

    // =========================================================
    // PASSAGE
    // =========================================================

    // Body: { "etudiantId": 1, "evaluationId": 2 }
    @PostMapping("/demarrer")
    public TentativeDTO demarrer(@RequestBody Map<String, Long> body) {
        Long etudiantId = body.get("etudiantId");
        verifierEtudiantConnecte(etudiantId);

        Tentative t = tentativeService.demarrerTentative(etudiantId, body.get("evaluationId"));
        return tentativeService.convertToDTO(t);
    }

    @PostMapping("/{id}/soumettre")
    public TentativeDTO soumettre(@PathVariable Long id) {
        Tentative tentative = tentativeService.getTentativeById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Tentative non trouvée"));
        verifierProprietaireTentative(tentative);

        Tentative soumise = notationService.soumettreTentative(id);
        return tentativeService.convertToDTO(soumise);
    }

    // =========================================================
    // ÉTAT
    // =========================================================

    @GetMapping("/etat")
    public String etat(@RequestParam Long etudiantId, @RequestParam Long evaluationId) {
        verifierEtudiantConnecte(etudiantId);
        return tentativeService.etatEvaluation(etudiantId, evaluationId);
    }

    @GetMapping("/restantes")
    public Integer restantes(@RequestParam Long etudiantId, @RequestParam Long evaluationId) {
        verifierEtudiantConnecte(etudiantId);
        return tentativeService.tentativesRestantes(etudiantId, evaluationId);
    }

    // =========================================================
    // NOTES
    // =========================================================

    @GetMapping("/note")
    public ResponseEntity<Double> noteEvaluation(@RequestParam Long etudiantId, @RequestParam Long evaluationId) {
        verifierEtudiantConnecte(etudiantId);
        Double note = notationService.noteEvaluationEtudiant(etudiantId, evaluationId);
        if (note == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(note);
    }
}
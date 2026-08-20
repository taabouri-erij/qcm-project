package com.qcm.backend.controller;

import com.qcm.backend.entity.Tentative;
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

    // =========================================================
    // CONSULTATION
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<Tentative> getTentativeById(@PathVariable Long id) {
        return tentativeService.getTentativeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/etudiant/{etudiantId}")
    public List<Tentative> getByEtudiant(@PathVariable Long etudiantId) {
        return tentativeService.getTentativesByEtudiant(etudiantId);
    }

    @GetMapping("/evaluation/{evaluationId}")
    public List<Tentative> getByEvaluation(@PathVariable Long evaluationId) {
        return tentativeService.getTentativesByEvaluation(evaluationId);
    }

    // =========================================================
    // PASSAGE
    // =========================================================

    // Body: { "etudiantId": 1, "evaluationId": 2 }
    @PostMapping("/demarrer")
    public ResponseEntity<?> demarrer(@RequestBody Map<String, Long> body) {
        try {
            Tentative t = tentativeService.demarrerTentative(
                    body.get("etudiantId"),
                    body.get("evaluationId")
            );
            return ResponseEntity.ok(t);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/soumettre")
    public ResponseEntity<?> soumettre(@PathVariable Long id) {
        try {
            Tentative tentative = notationService.soumettreTentative(id);
            return ResponseEntity.ok(tentative);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // =========================================================
    // ÉTAT
    // =========================================================

    @GetMapping("/etat")
    public ResponseEntity<String> etat(
            @RequestParam Long etudiantId,
            @RequestParam Long evaluationId) {
        return ResponseEntity.ok(
                tentativeService.etatEvaluation(etudiantId, evaluationId)
        );
    }

    @GetMapping("/restantes")
    public ResponseEntity<Integer> restantes(
            @RequestParam Long etudiantId,
            @RequestParam Long evaluationId) {
        return ResponseEntity.ok(
                tentativeService.tentativesRestantes(etudiantId, evaluationId)
        );
    }

    // =========================================================
    // NOTES
    // =========================================================

    @GetMapping("/note")
    public ResponseEntity<Double> noteEvaluation(
            @RequestParam Long etudiantId,
            @RequestParam Long evaluationId) {
        Double note = notationService.noteEvaluationEtudiant(etudiantId, evaluationId);
        if (note == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(note);
    }
}
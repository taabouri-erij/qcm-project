package com.qcm.backend.controller;

import com.qcm.backend.dto.EvaluationDTO;
import com.qcm.backend.entity.Evaluation;
import com.qcm.backend.service.EvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
@CrossOrigin(origins = "*")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @GetMapping
    public List<EvaluationDTO> getAllEvaluations() {
        return evaluationService.getAllEvaluations().stream()
                .map(evaluationService::convertToDTOSansQuestions).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationDTO> getEvaluationById(@PathVariable Long id) {
        return evaluationService.getEvaluationById(id)
                .map(evaluationService::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/chapitre/{chapitreId}")
    public List<EvaluationDTO> getByChapitre(@PathVariable Long chapitreId) {
        return evaluationService.getEvaluationsByChapitre(chapitreId).stream()
                .map(evaluationService::convertToDTOSansQuestions).toList();
    }

    @GetMapping("/matiere/{matiereId}")
    public List<EvaluationDTO> getByMatiere(@PathVariable Long matiereId) {
        return evaluationService.getEvaluationsByMatiere(matiereId).stream()
                .map(evaluationService::convertToDTOSansQuestions).toList();
    }

    @PostMapping
    public EvaluationDTO createEvaluation(@RequestBody Evaluation evaluation) {
        return evaluationService.convertToDTO(evaluationService.createEvaluation(evaluation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluationDTO> updateEvaluation(@PathVariable Long id, @RequestBody Evaluation details) {
        try {
            return ResponseEntity.ok(evaluationService.convertToDTO(evaluationService.updateEvaluation(id, details)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        evaluationService.deleteEvaluation(id);
        return ResponseEntity.noContent().build();
    }
}
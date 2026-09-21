package com.qcm.backend.controller;

import com.qcm.backend.dto.EvaluationDTO;
import com.qcm.backend.entity.Evaluation;
import com.qcm.backend.security.AuthUtils;
import com.qcm.backend.service.EvaluationService;
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
    public EvaluationDTO getEvaluationById(@PathVariable Long id) {
        Evaluation evaluation = evaluationService.getEvaluationById(id);
        // Un étudiant ne doit JAMAIS recevoir les bonnes réponses via cet endpoint
        if ("ETUDIANT".equals(AuthUtils.getRoleConnecte())) {
            return evaluationService.convertToDTOPourEtudiant(evaluation);
        }
        return evaluationService.convertToDTO(evaluation);
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
    public EvaluationDTO updateEvaluation(@PathVariable Long id, @RequestBody Evaluation details) {
        return evaluationService.convertToDTO(evaluationService.updateEvaluation(id, details));
    }

    @DeleteMapping("/{id}")
    public void deleteEvaluation(@PathVariable Long id) {
        evaluationService.deleteEvaluation(id);
    }
}
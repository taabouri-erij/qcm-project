package com.qcm.backend.controller;

import com.qcm.backend.entity.EvaluationQuestion;
import com.qcm.backend.service.EvaluationQuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/evaluation-questions")
@CrossOrigin(origins = "*")
public class EvaluationQuestionController {

    private final EvaluationQuestionService service;

    public EvaluationQuestionController(EvaluationQuestionService service) {
        this.service = service;
    }

    @GetMapping("/evaluation/{evaluationId}")
    public List<EvaluationQuestion> getByEvaluation(@PathVariable Long evaluationId) {
        return service.getByEvaluation(evaluationId);
    }

    // Body: { "evaluationId": 1, "questionId": 2, "points": 2.0, "ordre": 1 }
    @PostMapping
    public EvaluationQuestion ajouter(@RequestBody Map<String, Object> body) {
        Long evaluationId = Long.valueOf(body.get("evaluationId").toString());
        Long questionId = Long.valueOf(body.get("questionId").toString());
        Double points = body.get("points") != null ? Double.valueOf(body.get("points").toString()) : null;
        Integer ordre = body.get("ordre") != null ? Integer.valueOf(body.get("ordre").toString()) : null;
        return service.ajouterQuestion(evaluationId, questionId, points, ordre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluationQuestion> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            Double points = body.get("points") != null ? Double.valueOf(body.get("points").toString()) : null;
            Integer ordre = body.get("ordre") != null ? Integer.valueOf(body.get("ordre").toString()) : null;
            return ResponseEntity.ok(service.update(id, points, ordre));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
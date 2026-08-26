package com.qcm.backend.controller;

import com.qcm.backend.dto.EvaluationQuestionDTO;
import com.qcm.backend.entity.ReponseEtudiant;
import com.qcm.backend.service.EvaluationQuestionService;
import com.qcm.backend.service.PassageService;
import com.qcm.backend.service.ReponseEtudiantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/passage")
@CrossOrigin(origins = "*")
public class PassageController {

    private final PassageService passageService;
    private final EvaluationQuestionService evaluationQuestionService;
    private final ReponseEtudiantService reponseEtudiantService;

    public PassageController(PassageService passageService,
                             EvaluationQuestionService evaluationQuestionService,
                             ReponseEtudiantService reponseEtudiantService) {
        this.passageService = passageService;
        this.evaluationQuestionService = evaluationQuestionService;
        this.reponseEtudiantService = reponseEtudiantService;
    }

    // Body: { "tentativeId": 1, "evaluationQuestionId": 2, "reponsesPossiblesIds": [3, 5] }
    @PostMapping("/repondre")
    public ResponseEntity<?> repondre(@RequestBody Map<String, Object> body) {
        try {
            Long tentativeId = Long.valueOf(body.get("tentativeId").toString());
            Long evaluationQuestionId = Long.valueOf(body.get("evaluationQuestionId").toString());

            @SuppressWarnings("unchecked")
            List<Object> rawIds = (List<Object>) body.get("reponsesPossiblesIds");
            List<Long> ids = rawIds != null
                    ? rawIds.stream().map(o -> Long.valueOf(o.toString())).toList()
                    : List.of();

            ReponseEtudiant re = passageService.repondre(tentativeId, evaluationQuestionId, ids);
            return ResponseEntity.ok(reponseEtudiantService.convertToDTO(re));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/questions/{evaluationId}")
    public List<EvaluationQuestionDTO> getQuestions(@PathVariable Long evaluationId) {
        return passageService.getQuestionsEvaluation(evaluationId).stream()
                .map(evaluationQuestionService::convertToDTO)
                .toList();
    }
}
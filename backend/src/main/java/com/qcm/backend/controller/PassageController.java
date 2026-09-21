package com.qcm.backend.controller;

import com.qcm.backend.dto.EvaluationQuestionDTO;
import com.qcm.backend.entity.ReponseEtudiant;
import com.qcm.backend.entity.Tentative;
import com.qcm.backend.exception.RessourceNonTrouveeException;
import com.qcm.backend.security.AuthUtils;
import com.qcm.backend.service.EvaluationQuestionService;
import com.qcm.backend.service.PassageService;
import com.qcm.backend.service.ReponseEtudiantService;
import com.qcm.backend.service.TentativeService;
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
    private final TentativeService tentativeService;

    public PassageController(PassageService passageService,
                             EvaluationQuestionService evaluationQuestionService,
                             ReponseEtudiantService reponseEtudiantService,
                             TentativeService tentativeService) {
        this.passageService = passageService;
        this.evaluationQuestionService = evaluationQuestionService;
        this.reponseEtudiantService = reponseEtudiantService;
        this.tentativeService = tentativeService;
    }

    private void verifierProprietaireTentative(Long tentativeId) {
        if (AuthUtils.estEnseignantOuAdmin()) {
            return;
        }
        Tentative tentative = tentativeService.getTentativeById(tentativeId)
                .orElseThrow(() -> new RessourceNonTrouveeException("Tentative non trouvée"));

        Long userIdConnecte = AuthUtils.getUserIdConnecte();
        if (!tentative.getEtudiant().getId().equals(userIdConnecte)) {
            throw new RessourceNonTrouveeException("Tentative non trouvée");
        }
    }

    @PostMapping("/repondre")
    public Object repondre(@RequestBody Map<String, Object> body) {
        Long tentativeId = Long.valueOf(body.get("tentativeId").toString());
        verifierProprietaireTentative(tentativeId);

        Long evaluationQuestionId = Long.valueOf(body.get("evaluationQuestionId").toString());

        @SuppressWarnings("unchecked")
        List<Object> rawIds = (List<Object>) body.get("reponsesPossiblesIds");
        List<Long> ids = rawIds != null
                ? rawIds.stream().map(o -> Long.valueOf(o.toString())).toList()
                : List.of();

        ReponseEtudiant re = passageService.repondre(tentativeId, evaluationQuestionId, ids);
        return reponseEtudiantService.convertToDTO(re);
    }

    @GetMapping("/questions/{evaluationId}")
    public List<EvaluationQuestionDTO> getQuestions(@PathVariable Long evaluationId) {
        // Toujours SANS les bonnes réponses : cet endpoint sert à passer l'examen
        return passageService.getQuestionsEvaluation(evaluationId).stream()
                .map(evaluationQuestionService::convertToDTOPourEtudiant)
                .toList();
    }
}
package com.qcm.backend.service;

import com.qcm.backend.entity.*;
import com.qcm.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PassageService {

    private final TentativeRepository tentativeRepository;
    private final EvaluationQuestionRepository evaluationQuestionRepository;
    private final ReponseEtudiantRepository reponseEtudiantRepository;
    private final ReponseEtudiantChoixRepository reponseEtudiantChoixRepository;
    private final ReponsePossibleRepository reponsePossibleRepository;
    private final NotationService notationService;

    public PassageService(TentativeRepository tentativeRepository,
                          EvaluationQuestionRepository evaluationQuestionRepository,
                          ReponseEtudiantRepository reponseEtudiantRepository,
                          ReponseEtudiantChoixRepository reponseEtudiantChoixRepository,
                          ReponsePossibleRepository reponsePossibleRepository,
                          NotationService notationService) {
        this.tentativeRepository = tentativeRepository;
        this.evaluationQuestionRepository = evaluationQuestionRepository;
        this.reponseEtudiantRepository = reponseEtudiantRepository;
        this.reponseEtudiantChoixRepository = reponseEtudiantChoixRepository;
        this.reponsePossibleRepository = reponsePossibleRepository;
        this.notationService = notationService;
    }

    // =========================================================
    // RÉPONDRE À UNE QUESTION
    // Body attendu conceptuellement :
    // tentativeId, evaluationQuestionId, liste des ids des réponses choisies
    // =========================================================
    @Transactional
    public ReponseEtudiant repondre(Long tentativeId,
                                    Long evaluationQuestionId,
                                    List<Long> reponsesPossiblesIds) {

        Tentative tentative = tentativeRepository.findById(tentativeId)
                .orElseThrow(() -> new RuntimeException("Tentative non trouvée"));

        if (!"EN_COURS".equals(tentative.getStatut())) {
            throw new RuntimeException("Cette tentative n'est plus modifiable");
        }

        EvaluationQuestion eq = evaluationQuestionRepository.findById(evaluationQuestionId)
                .orElseThrow(() -> new RuntimeException("Question d'évaluation non trouvée"));

        // Chercher s'il existe déjà une réponse pour cette question dans cette tentative
        List<ReponseEtudiant> existantes = reponseEtudiantRepository.findByTentativeId(tentativeId);
        ReponseEtudiant reponseEtudiant = existantes.stream()
                .filter(r -> r.getEvaluationQuestion().getId().equals(evaluationQuestionId))
                .findFirst()
                .orElse(null);

        if (reponseEtudiant == null) {
            reponseEtudiant = new ReponseEtudiant();
            reponseEtudiant.setTentative(tentative);
            reponseEtudiant.setEvaluationQuestion(eq);
        } else {
            // Supprimer les anciens choix
            reponseEtudiant.getChoix().clear();
        }

        // Enregistrer les nouveaux choix
        List<ReponsePossible> choix = new ArrayList<>();
        if (reponsesPossiblesIds != null) {
            for (Long id : reponsesPossiblesIds) {
                ReponsePossible rp = reponsePossibleRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Réponse possible non trouvée: " + id));
                choix.add(rp);

                ReponseEtudiantChoix rec = new ReponseEtudiantChoix();
                rec.setReponseEtudiant(reponseEtudiant);
                rec.setReponsePossible(rp);
                reponseEtudiant.getChoix().add(rec);
            }
        }

        // Calculer le score de la question
        double score = notationService.calculerNoteQuestion(eq, choix);
        reponseEtudiant.setScoreQuestion(score);

        return reponseEtudiantRepository.save(reponseEtudiant);
    }

    // Questions d'une évaluation (pour affichage étudiant)
    public List<EvaluationQuestion> getQuestionsEvaluation(Long evaluationId) {
        return evaluationQuestionRepository.findByEvaluationId(evaluationId);
    }
}
package com.qcm.backend.service;

import com.qcm.backend.entity.*;
import com.qcm.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TentativeService {

    private final TentativeRepository tentativeRepository;
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final EtudiantMatiereRepository etudiantMatiereRepository;
    private final EvaluationQuestionRepository evaluationQuestionRepository;

    public TentativeService(TentativeRepository tentativeRepository,
                            EvaluationRepository evaluationRepository,
                            UserRepository userRepository,
                            EtudiantMatiereRepository etudiantMatiereRepository,
                            EvaluationQuestionRepository evaluationQuestionRepository) {
        this.tentativeRepository = tentativeRepository;
        this.evaluationRepository = evaluationRepository;
        this.userRepository = userRepository;
        this.etudiantMatiereRepository = etudiantMatiereRepository;
        this.evaluationQuestionRepository = evaluationQuestionRepository;
    }

    public List<Tentative> getAllTentatives() {
        return tentativeRepository.findAll();
    }

    public Optional<Tentative> getTentativeById(Long id) {
        return tentativeRepository.findById(id);
    }

    public List<Tentative> getTentativesByEtudiant(Long etudiantId) {
        return tentativeRepository.findByEtudiantId(etudiantId);
    }

    public List<Tentative> getTentativesByEvaluation(Long evaluationId) {
        return tentativeRepository.findByEvaluationId(evaluationId);
    }

    public Tentative createTentative(Tentative tentative) {
        return tentativeRepository.save(tentative);
    }

    public Tentative updateTentative(Long id, Tentative details) {
        Tentative tentative = tentativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tentative non trouvée"));
        tentative.setDateFin(details.getDateFin());
        tentative.setScore(details.getScore());
        tentative.setStatut(details.getStatut());
        return tentativeRepository.save(tentative);
    }

    public void deleteTentative(Long id) {
        tentativeRepository.deleteById(id);
    }

    // =========================================================
    // DÉMARRER UNE TENTATIVE (avec toutes les vérifications)
    // =========================================================
    @Transactional
    public Tentative demarrerTentative(Long etudiantId, Long evaluationId) {
        User etudiant = userRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Évaluation non trouvée"));

        // 1. Évaluation publiée ?
        if (!Boolean.TRUE.equals(evaluation.getPublie())) {
            throw new RuntimeException("Cette évaluation n'est pas disponible");
        }

        // 2. Dans la fenêtre de temps ?
        LocalDateTime maintenant = LocalDateTime.now();
        if (evaluation.getDateDebut() != null && maintenant.isBefore(evaluation.getDateDebut())) {
            throw new RuntimeException("Cette évaluation n'a pas encore commencé");
        }
        if (evaluation.getDateFin() != null && maintenant.isAfter(evaluation.getDateFin())) {
            throw new RuntimeException("Cette évaluation est terminée");
        }

        // 3. Étudiant inscrit à la matière ?
        Long matiereId = null;
        if (evaluation.getMatiere() != null) {
            matiereId = evaluation.getMatiere().getId();
        } else if (evaluation.getChapitre() != null
                && evaluation.getChapitre().getMatiere() != null) {
            matiereId = evaluation.getChapitre().getMatiere().getId();
        }

        if (matiereId != null) {
            final Long finalMatiereId = matiereId;
            boolean inscrit = etudiantMatiereRepository.findByEtudiantId(etudiantId).stream()
                    .anyMatch(em -> em.getMatiere().getId().equals(finalMatiereId));
            if (!inscrit) {
                throw new RuntimeException("Vous n'êtes pas inscrit à cette matière");
            }
        }

        // 4. Pas de tentative déjà EN_COURS
        List<Tentative> existantes = tentativeRepository
                .findByEtudiantIdAndEvaluationId(etudiantId, evaluationId);

        boolean enCours = existantes.stream()
                .anyMatch(t -> "EN_COURS".equals(t.getStatut()));
        if (enCours) {
            throw new RuntimeException("Vous avez déjà une tentative en cours");
        }

        // 5. Nombre de tentatives max
        long nbDejaFaites = existantes.stream()
                .filter(t -> !"ANNULE".equals(t.getStatut()))
                .count();
        if (nbDejaFaites >= evaluation.getNombreTentativesMax()) {
            throw new RuntimeException("Nombre maximal de tentatives atteint");
        }

        // Créer la tentative
        Tentative tentative = new Tentative();
        tentative.setEtudiant(etudiant);
        tentative.setEvaluation(evaluation);
        tentative.setDateDebut(LocalDateTime.now());
        tentative.setStatut("EN_COURS");

        return tentativeRepository.save(tentative);
    }

    // =========================================================
    // ÉTAT D'UNE ÉVALUATION POUR UN ÉTUDIANT
    // =========================================================
    public String etatEvaluation(Long etudiantId, Long evaluationId) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Évaluation non trouvée"));

        LocalDateTime maintenant = LocalDateTime.now();

        if (evaluation.getDateDebut() != null && maintenant.isBefore(evaluation.getDateDebut())) {
            return "A_VENIR";
        }
        if (evaluation.getDateFin() != null && maintenant.isAfter(evaluation.getDateFin())) {
            return "TERMINE";
        }

        List<Tentative> tentatives = tentativeRepository
                .findByEtudiantIdAndEvaluationId(etudiantId, evaluationId);

        boolean enCours = tentatives.stream()
                .anyMatch(t -> "EN_COURS".equals(t.getStatut()));
        if (enCours) {
            return "EN_COURS";
        }

        return "DISPONIBLE";
    }

    // =========================================================
    // NOMBRE DE TENTATIVES RESTANTES
    // =========================================================
    public int tentativesRestantes(Long etudiantId, Long evaluationId) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Évaluation non trouvée"));

        long nbDejaFaites = tentativeRepository
                .findByEtudiantIdAndEvaluationId(etudiantId, evaluationId).stream()
                .filter(t -> !"ANNULE".equals(t.getStatut()))
                .count();

        return (int) (evaluation.getNombreTentativesMax() - nbDejaFaites);
    }
}
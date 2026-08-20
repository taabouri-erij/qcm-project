package com.qcm.backend.service;

import com.qcm.backend.entity.*;
import com.qcm.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotationService {

    private final TentativeRepository tentativeRepository;
    private final ReponseEtudiantRepository reponseEtudiantRepository;
    private final ReponseEtudiantChoixRepository reponseEtudiantChoixRepository;
    private final AlerteRepository alerteRepository;
    private final EvaluationQuestionRepository evaluationQuestionRepository;
    private final EvaluationRepository evaluationRepository;
    private final EtudiantMatiereRepository etudiantMatiereRepository;
    private final MatiereRepository matiereRepository;
    private final ModuleRepository moduleRepository;

    public NotationService(TentativeRepository tentativeRepository,
                           ReponseEtudiantRepository reponseEtudiantRepository,
                           ReponseEtudiantChoixRepository reponseEtudiantChoixRepository,
                           AlerteRepository alerteRepository,
                           EvaluationQuestionRepository evaluationQuestionRepository,
                           EvaluationRepository evaluationRepository,
                           EtudiantMatiereRepository etudiantMatiereRepository,
                           MatiereRepository matiereRepository,
                           ModuleRepository moduleRepository) {
        this.tentativeRepository = tentativeRepository;
        this.reponseEtudiantRepository = reponseEtudiantRepository;
        this.reponseEtudiantChoixRepository = reponseEtudiantChoixRepository;
        this.alerteRepository = alerteRepository;
        this.evaluationQuestionRepository = evaluationQuestionRepository;
        this.evaluationRepository = evaluationRepository;
        this.etudiantMatiereRepository = etudiantMatiereRepository;
        this.matiereRepository = matiereRepository;
        this.moduleRepository = moduleRepository;
    }

    // =========================================================
    // 1. CALCUL NOTE D'UNE QUESTION
    // Règle :
    // - Si au moins 1 réponse fausse cochée → 0
    // - Sinon → (nb bonnes cochées / nb total bonnes) × points
    // =========================================================
    public double calculerNoteQuestion(EvaluationQuestion eq, List<ReponsePossible> choixEtudiant) {
        Question question = eq.getQuestion();
        double pointsMax = eq.getPoints();

        List<ReponsePossible> bonnesReponses = question.getReponsesPossibles().stream()
                .filter(ReponsePossible::getEstCorrecte)
                .collect(Collectors.toList());

        if (bonnesReponses.isEmpty()) {
            return 0;
        }

        Set<Long> idsBonnes = bonnesReponses.stream()
                .map(ReponsePossible::getId)
                .collect(Collectors.toSet());

        Set<Long> idsChoisis = choixEtudiant.stream()
                .map(ReponsePossible::getId)
                .collect(Collectors.toSet());

        // Au moins une mauvaise réponse cochée → 0
        boolean aUneFausse = idsChoisis.stream().anyMatch(id -> !idsBonnes.contains(id));
        if (aUneFausse) {
            return 0;
        }

        // Uniquement des bonnes → note proportionnelle
        long nbBonnesCochees = idsChoisis.stream().filter(idsBonnes::contains).count();
        return (nbBonnesCochees / (double) bonnesReponses.size()) * pointsMax;
    }

    // =========================================================
    // 2. ENREGISTRER UNE ALERTE + SANCTIONS
    // - Alerte avec question_id → question à 0
    // - Alerte globale (question_id NULL) → compte pour les 3
    // - ≥ 3 alertes globales → tentative à 0 (ANNULE)
    // =========================================================
    @Transactional
    public Alerte enregistrerAlerte(Alerte alerte) {
        Alerte saved = alerteRepository.save(alerte);
        Tentative tentative = alerte.getTentative();

        // Cas 1 : alerte liée à une question → cette question à 0
        if (alerte.getQuestion() != null) {
            List<ReponseEtudiant> reponses = reponseEtudiantRepository.findByTentativeId(tentative.getId());
            for (ReponseEtudiant re : reponses) {
                if (re.getEvaluationQuestion().getQuestion().getId()
                        .equals(alerte.getQuestion().getId())) {
                    re.setScoreQuestion(0.0);
                    reponseEtudiantRepository.save(re);
                }
            }
        }

        // Cas 2 : compter UNIQUEMENT les alertes globales
        long nbAlertesGlobales = alerteRepository.findByTentativeId(tentative.getId()).stream()
                .filter(a -> a.getQuestion() == null)
                .count();

        if (nbAlertesGlobales >= 3) {
            tentative.setScore(0.0);
            tentative.setStatut("ANNULE");
            tentativeRepository.save(tentative);
        }

        return saved;
    }

    // =========================================================
    // 3. SOUMETTRE UNE TENTATIVE (correction automatique)
    // =========================================================
    @Transactional
    public Tentative soumettreTentative(Long tentativeId) {
        Tentative tentative = tentativeRepository.findById(tentativeId)
                .orElseThrow(() -> new RuntimeException("Tentative non trouvée"));

        // Vérifier que la tentative est bien EN_COURS
        if (!"EN_COURS".equals(tentative.getStatut())) {
            throw new RuntimeException(
                    "Cette tentative ne peut plus être soumise (statut: " + tentative.getStatut() + ")"
            );
        }

        // Vérifier le nombre d'alertes globales
        long nbAlertesGlobales = alerteRepository.findByTentativeId(tentativeId).stream()
                .filter(a -> a.getQuestion() == null)
                .count();

        if (nbAlertesGlobales >= 3) {
            tentative.setScore(0.0);
            tentative.setStatut("ANNULE");
            tentative.setDateFin(LocalDateTime.now());
            return tentativeRepository.save(tentative);
        }

        // Vérifier si la date limite est dépassée
        Evaluation evaluation = tentative.getEvaluation();
        if (evaluation.getDateFin() != null && LocalDateTime.now().isAfter(evaluation.getDateFin())) {
            tentative.setScore(0.0);
            tentative.setStatut("EXPIRE");
            tentative.setDateFin(LocalDateTime.now());
            return tentativeRepository.save(tentative);
        }

        // Calculer le score total
        List<ReponseEtudiant> reponses = reponseEtudiantRepository.findByTentativeId(tentativeId);
        double scoreTotal = 0;
        double pointsMaxTotal = 0;

        for (ReponseEtudiant re : reponses) {
            if (re.getScoreQuestion() != null) {
                scoreTotal += re.getScoreQuestion();
            }
            pointsMaxTotal += re.getEvaluationQuestion().getPoints();
        }

        // Ramener sur 20
        double scoreSur20 = (pointsMaxTotal > 0) ? (scoreTotal / pointsMaxTotal) * 20.0 : 0;

        tentative.setScore(Math.round(scoreSur20 * 100.0) / 100.0);
        tentative.setStatut("SOUMIS");
        tentative.setDateFin(LocalDateTime.now());

        return tentativeRepository.save(tentative);
    }

    // =========================================================
    // 4. NOTE D'UNE ÉVALUATION POUR UN ÉTUDIANT
    // = moyenne de ses tentatives SOUMISES
    // =========================================================
    public Double noteEvaluationEtudiant(Long etudiantId, Long evaluationId) {
        List<Tentative> tentatives = tentativeRepository
                .findByEtudiantIdAndEvaluationId(etudiantId, evaluationId);

        List<Tentative> valides = tentatives.stream()
                .filter(t -> "SOUMIS".equals(t.getStatut()) && t.getScore() != null)
                .collect(Collectors.toList());

        if (valides.isEmpty()) {
            return null;
        }

        double moyenne = valides.stream()
                .mapToDouble(Tentative::getScore)
                .average()
                .orElse(0);

        return Math.round(moyenne * 100.0) / 100.0;
    }

    // =========================================================
    // 5. NOTE D'UNE MATIÈRE
    // = (moyenne examens chapitre × 1 + examen final × 2) / 3
    // =========================================================
    public Double noteMatiere(Long etudiantId, Long matiereId) {

        // Examens de chapitre (type EXAMEN liés aux chapitres de la matière)
        List<Evaluation> examensChapitre = evaluationRepository.findAll().stream()
                .filter(e -> "EXAMEN".equals(e.getType())
                        && e.getChapitre() != null
                        && e.getChapitre().getMatiere() != null
                        && e.getChapitre().getMatiere().getId().equals(matiereId))
                .collect(Collectors.toList());

        // Examens finaux de la matière
        List<Evaluation> examensFinaux = evaluationRepository.findByMatiereId(matiereId).stream()
                .filter(e -> "EXAMEN_FINAL".equals(e.getType()))
                .collect(Collectors.toList());

        // Moyenne des examens de chapitre
        double sommeExamens = 0;
        int nbExamens = 0;
        for (Evaluation ev : examensChapitre) {
            Double note = noteEvaluationEtudiant(etudiantId, ev.getId());
            if (note != null) {
                sommeExamens += note;
                nbExamens++;
            }
        }

        // Note de l'examen final
        Double noteFinal = null;
        if (!examensFinaux.isEmpty()) {
            noteFinal = noteEvaluationEtudiant(etudiantId, examensFinaux.get(0).getId());
        }

        // Pas d'examen final passé → note matière non calculable
        if (noteFinal == null) {
            return null;
        }

        double moyExamens = (nbExamens > 0) ? (sommeExamens / nbExamens) : 0;

        // Formule validée : (moyExamens × 1 + final × 2) / 3
        double noteMatiere = (moyExamens * 1 + noteFinal * 2) / 3.0;
        return Math.round(noteMatiere * 100.0) / 100.0;
    }

    // =========================================================
    // 6. NOTE D'UN MODULE
    // = moyenne des notes des matières
    // Calculée seulement si TOUS les examens finaux sont passés
    // =========================================================
    public Double noteModule(Long etudiantId, Long moduleId) {
        List<Matiere> matieres = matiereRepository.findByModuleId(moduleId);

        double somme = 0;
        int count = 0;

        for (Matiere matiere : matieres) {
            Double note = noteMatiere(etudiantId, matiere.getId());
            if (note == null) {
                return null; // il manque au moins un examen final
            }
            somme += note;
            count++;
        }

        if (count == 0) {
            return null;
        }

        return Math.round((somme / count) * 100.0) / 100.0;
    }
}
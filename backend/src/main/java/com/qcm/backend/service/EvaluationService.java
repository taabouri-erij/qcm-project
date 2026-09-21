package com.qcm.backend.service;

import com.qcm.backend.dto.EvaluationDTO;
import com.qcm.backend.entity.Evaluation;
import com.qcm.backend.exception.RessourceNonTrouveeException;
import com.qcm.backend.repository.EvaluationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final EvaluationQuestionService evaluationQuestionService;

    public EvaluationService(EvaluationRepository evaluationRepository,
                             EvaluationQuestionService evaluationQuestionService) {
        this.evaluationRepository = evaluationRepository;
        this.evaluationQuestionService = evaluationQuestionService;
    }

    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    public Evaluation getEvaluationById(Long id) {
        return evaluationRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Évaluation non trouvée"));
    }

    public List<Evaluation> getEvaluationsByChapitre(Long chapitreId) {
        return evaluationRepository.findByChapitreId(chapitreId);
    }

    public List<Evaluation> getEvaluationsByMatiere(Long matiereId) {
        return evaluationRepository.findByMatiereId(matiereId);
    }

    public Evaluation createEvaluation(Evaluation evaluation) {
        return evaluationRepository.save(evaluation);
    }

    public Evaluation updateEvaluation(Long id, Evaluation details) {
        Evaluation evaluation = getEvaluationById(id);
        evaluation.setTitre(details.getTitre());
        evaluation.setType(details.getType());
        evaluation.setDateDebut(details.getDateDebut());
        evaluation.setDateFin(details.getDateFin());
        evaluation.setDureeMinutes(details.getDureeMinutes());
        evaluation.setNombreTentativesMax(details.getNombreTentativesMax());
        evaluation.setOrdreAleatoire(details.getOrdreAleatoire());
        evaluation.setPublie(details.getPublie());
        return evaluationRepository.save(evaluation);
    }

    public void deleteEvaluation(Long id) {
        evaluationRepository.deleteById(id);
    }

    private EvaluationDTO baseDTO(Evaluation evaluation) {
        EvaluationDTO dto = new EvaluationDTO();
        dto.setId(evaluation.getId());
        dto.setTitre(evaluation.getTitre());
        dto.setType(evaluation.getType());
        dto.setDateDebut(evaluation.getDateDebut());
        dto.setDateFin(evaluation.getDateFin());
        dto.setDureeMinutes(evaluation.getDureeMinutes());
        dto.setNombreTentativesMax(evaluation.getNombreTentativesMax());
        dto.setOrdreAleatoire(evaluation.getOrdreAleatoire());
        dto.setPublie(evaluation.getPublie());
        if (evaluation.getChapitre() != null) {
            dto.setChapitreId(evaluation.getChapitre().getId());
            dto.setChapitreTitre(evaluation.getChapitre().getTitre());
        }
        if (evaluation.getMatiere() != null) {
            dto.setMatiereId(evaluation.getMatiere().getId());
            dto.setMatiereNom(evaluation.getMatiere().getNom());
        }
        return dto;
    }

    // Version complète, AVEC les bonnes réponses (enseignant/admin)
    public EvaluationDTO convertToDTO(Evaluation evaluation) {
        EvaluationDTO dto = baseDTO(evaluation);
        if (evaluation.getEvaluationQuestions() != null) {
            dto.setQuestions(
                    evaluation.getEvaluationQuestions().stream()
                            .map(evaluationQuestionService::convertToDTO)
                            .toList()
            );
        }
        return dto;
    }

    // Version SANS les bonnes réponses (étudiant en train de passer un examen)
    public EvaluationDTO convertToDTOPourEtudiant(Evaluation evaluation) {
        EvaluationDTO dto = baseDTO(evaluation);
        if (evaluation.getEvaluationQuestions() != null) {
            dto.setQuestions(
                    evaluation.getEvaluationQuestions().stream()
                            .map(evaluationQuestionService::convertToDTOPourEtudiant)
                            .toList()
            );
        }
        return dto;
    }

    // Version "légère" sans le détail des questions (pour les listes)
    public EvaluationDTO convertToDTOSansQuestions(Evaluation evaluation) {
        return baseDTO(evaluation);
    }
}
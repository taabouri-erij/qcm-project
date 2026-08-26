package com.qcm.backend.service;

import com.qcm.backend.dto.EvaluationDTO;
import com.qcm.backend.entity.Evaluation;
import com.qcm.backend.repository.EvaluationRepository;
import org.springframework.stereotype.Service;
import com.qcm.backend.exception.RessourceNonTrouveeException;
import java.util.List;
import java.util.Optional;

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

    public Optional<Evaluation> getEvaluationById(Long id) {
        return evaluationRepository.findById(id);
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
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Évaluation non trouvée"));
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

    public EvaluationDTO convertToDTO(Evaluation evaluation) {
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
        if (evaluation.getEvaluationQuestions() != null) {
            dto.setQuestions(
                    evaluation.getEvaluationQuestions().stream()
                            .map(evaluationQuestionService::convertToDTO)
                            .toList()
            );
        }
        return dto;
    }

    // Version "légère" sans le détail des questions (pour les listes)
    public EvaluationDTO convertToDTOSansQuestions(Evaluation evaluation) {
        EvaluationDTO dto = convertToDTO(evaluation);
        dto.setQuestions(null);
        return dto;
    }
}
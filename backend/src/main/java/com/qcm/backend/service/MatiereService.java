package com.qcm.backend.service;

import com.qcm.backend.dto.MatiereDTO;
import com.qcm.backend.entity.Matiere;
import com.qcm.backend.repository.MatiereRepository;
import org.springframework.stereotype.Service;
import com.qcm.backend.exception.RessourceNonTrouveeException;
import java.util.List;
import java.util.Optional;

@Service
public class MatiereService {

    private final MatiereRepository matiereRepository;

    public MatiereService(MatiereRepository matiereRepository) {
        this.matiereRepository = matiereRepository;
    }

    public List<Matiere> getAllMatieres() {
        return matiereRepository.findAll();
    }

    public Optional<Matiere> getMatiereById(Long id) {
        return matiereRepository.findById(id);
    }

    public List<Matiere> getMatieresByModule(Long moduleId) {
        return matiereRepository.findByModuleId(moduleId);
    }

    public Matiere createMatiere(Matiere matiere) {
        return matiereRepository.save(matiere);
    }

    public Matiere updateMatiere(Long id, Matiere details) {
        Matiere matiere = matiereRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Matière non trouvée"));
        matiere.setNom(details.getNom());
        matiere.setDescription(details.getDescription());
        matiere.setModule(details.getModule());
        return matiereRepository.save(matiere);
    }

    public void deleteMatiere(Long id) {
        matiereRepository.deleteById(id);
    }

    public MatiereDTO convertToDTO(Matiere matiere) {
        MatiereDTO dto = new MatiereDTO();
        dto.setId(matiere.getId());
        dto.setNom(matiere.getNom());
        dto.setDescription(matiere.getDescription());
        if (matiere.getModule() != null) {
            dto.setModuleId(matiere.getModule().getId());
            dto.setModuleNom(matiere.getModule().getNom());
        }
        return dto;
    }
}
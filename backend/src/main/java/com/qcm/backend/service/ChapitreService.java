package com.qcm.backend.service;

import com.qcm.backend.entity.Chapitre;
import com.qcm.backend.repository.ChapitreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChapitreService {

    private final ChapitreRepository chapitreRepository;

    public ChapitreService(ChapitreRepository chapitreRepository) {
        this.chapitreRepository = chapitreRepository;
    }

    public List<Chapitre> getAllChapitres() {
        return chapitreRepository.findAll();
    }

    public Optional<Chapitre> getChapitreById(Long id) {
        return chapitreRepository.findById(id);
    }

    public List<Chapitre> getChapitresByMatiere(Long matiereId) {
        return chapitreRepository.findByMatiereId(matiereId);
    }

    public Chapitre createChapitre(Chapitre chapitre) {
        return chapitreRepository.save(chapitre);
    }

    public Chapitre updateChapitre(Long id, Chapitre details) {
        Chapitre chapitre = chapitreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapitre non trouvé"));
        chapitre.setTitre(details.getTitre());
        chapitre.setNumero(details.getNumero());
        chapitre.setDescription(details.getDescription());
        chapitre.setMatiere(details.getMatiere());
        return chapitreRepository.save(chapitre);
    }

    public void deleteChapitre(Long id) {
        chapitreRepository.deleteById(id);
    }
}
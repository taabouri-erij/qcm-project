package com.qcm.backend.service;

import com.qcm.backend.entity.ReponseEtudiant;
import com.qcm.backend.repository.ReponseEtudiantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReponseEtudiantService {

    private final ReponseEtudiantRepository repository;

    public ReponseEtudiantService(ReponseEtudiantRepository repository) {
        this.repository = repository;
    }

    public List<ReponseEtudiant> getByTentative(Long tentativeId) {
        return repository.findByTentativeId(tentativeId);
    }

    public Optional<ReponseEtudiant> getById(Long id) {
        return repository.findById(id);
    }

    public ReponseEtudiant create(ReponseEtudiant reponse) {
        return repository.save(reponse);
    }

    public ReponseEtudiant update(Long id, ReponseEtudiant details) {
        ReponseEtudiant r = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réponse non trouvée"));
        r.setScoreQuestion(details.getScoreQuestion());
        return repository.save(r);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
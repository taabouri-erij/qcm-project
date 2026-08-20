package com.qcm.backend.service;

import com.qcm.backend.entity.ReponsePossible;
import com.qcm.backend.repository.ReponsePossibleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReponsePossibleService {

    private final ReponsePossibleRepository repository;

    public ReponsePossibleService(ReponsePossibleRepository repository) {
        this.repository = repository;
    }

    public List<ReponsePossible> getByQuestion(Long questionId) {
        return repository.findByQuestionId(questionId);
    }

    public Optional<ReponsePossible> getById(Long id) {
        return repository.findById(id);
    }

    public ReponsePossible create(ReponsePossible reponse) {
        return repository.save(reponse);
    }

    public ReponsePossible update(Long id, ReponsePossible details) {
        ReponsePossible r = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réponse non trouvée"));
        r.setTexte(details.getTexte());
        r.setEstCorrecte(details.getEstCorrecte());
        r.setOrdre(details.getOrdre());
        return repository.save(r);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
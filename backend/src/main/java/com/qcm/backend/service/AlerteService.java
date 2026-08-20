package com.qcm.backend.service;

import com.qcm.backend.entity.Alerte;
import com.qcm.backend.repository.AlerteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlerteService {

    private final AlerteRepository repository;

    public AlerteService(AlerteRepository repository) {
        this.repository = repository;
    }

    public List<Alerte> getByTentative(Long tentativeId) {
        return repository.findByTentativeId(tentativeId);
    }

    public long countByTentative(Long tentativeId) {
        return repository.countByTentativeId(tentativeId);
    }

    public Alerte create(Alerte alerte) {
        return repository.save(alerte);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
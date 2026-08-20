package com.qcm.backend.service;

import com.qcm.backend.entity.RessourcePedagogique;
import com.qcm.backend.repository.RessourcePedagogiqueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RessourcePedagogiqueService {

    private final RessourcePedagogiqueRepository repository;

    public RessourcePedagogiqueService(RessourcePedagogiqueRepository repository) {
        this.repository = repository;
    }

    public List<RessourcePedagogique> getAll() {
        return repository.findAll();
    }

    public Optional<RessourcePedagogique> getById(Long id) {
        return repository.findById(id);
    }

    public List<RessourcePedagogique> getByChapitre(Long chapitreId) {
        return repository.findByChapitreId(chapitreId);
    }

    public RessourcePedagogique create(RessourcePedagogique ressource) {
        return repository.save(ressource);
    }

    public RessourcePedagogique update(Long id, RessourcePedagogique details) {
        RessourcePedagogique r = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ressource non trouvée"));
        r.setTitre(details.getTitre());
        r.setContenu(details.getContenu());
        r.setType(details.getType());
        r.setFichierUrl(details.getFichierUrl());
        r.setChapitre(details.getChapitre());
        return repository.save(r);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
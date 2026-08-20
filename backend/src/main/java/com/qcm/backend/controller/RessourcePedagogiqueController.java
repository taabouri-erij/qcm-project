package com.qcm.backend.controller;

import com.qcm.backend.entity.RessourcePedagogique;
import com.qcm.backend.service.RessourcePedagogiqueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ressources")
@CrossOrigin(origins = "*")
public class RessourcePedagogiqueController {

    private final RessourcePedagogiqueService service;

    public RessourcePedagogiqueController(RessourcePedagogiqueService service) {
        this.service = service;
    }

    @GetMapping
    public List<RessourcePedagogique> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RessourcePedagogique> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/chapitre/{chapitreId}")
    public List<RessourcePedagogique> getByChapitre(@PathVariable Long chapitreId) {
        return service.getByChapitre(chapitreId);
    }

    @PostMapping
    public RessourcePedagogique create(@RequestBody RessourcePedagogique ressource) {
        return service.create(ressource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RessourcePedagogique> update(@PathVariable Long id, @RequestBody RessourcePedagogique details) {
        try {
            return ResponseEntity.ok(service.update(id, details));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
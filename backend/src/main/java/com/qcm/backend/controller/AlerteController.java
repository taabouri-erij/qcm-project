package com.qcm.backend.controller;

import com.qcm.backend.entity.Alerte;
import com.qcm.backend.service.AlerteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alertes")
@CrossOrigin(origins = "*")
public class AlerteController {

    private final AlerteService service;

    public AlerteController(AlerteService service) {
        this.service = service;
    }

    @GetMapping("/tentative/{tentativeId}")
    public List<Alerte> getByTentative(@PathVariable Long tentativeId) {
        return service.getByTentative(tentativeId);
    }

    @GetMapping("/tentative/{tentativeId}/count")
    public Map<String, Long> countByTentative(@PathVariable Long tentativeId) {
        return Map.of("nombreAlertes", service.countByTentative(tentativeId));
    }

    @PostMapping
    public Alerte create(@RequestBody Alerte alerte) {
        return service.create(alerte);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
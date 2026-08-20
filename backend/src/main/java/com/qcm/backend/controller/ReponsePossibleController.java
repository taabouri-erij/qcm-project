package com.qcm.backend.controller;

import com.qcm.backend.entity.ReponsePossible;
import com.qcm.backend.service.ReponsePossibleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reponses-possibles")
@CrossOrigin(origins = "*")
public class ReponsePossibleController {

    private final ReponsePossibleService service;

    public ReponsePossibleController(ReponsePossibleService service) {
        this.service = service;
    }

    @GetMapping("/question/{questionId}")
    public List<ReponsePossible> getByQuestion(@PathVariable Long questionId) {
        return service.getByQuestion(questionId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReponsePossible> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ReponsePossible create(@RequestBody ReponsePossible reponse) {
        return service.create(reponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReponsePossible> update(@PathVariable Long id, @RequestBody ReponsePossible details) {
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
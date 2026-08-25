package com.qcm.backend.controller;

import com.qcm.backend.dto.ModuleDTO;
import com.qcm.backend.entity.Module;
import com.qcm.backend.service.ModuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
@CrossOrigin(origins = "*")
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping
    public List<ModuleDTO> getAllModules() {
        return moduleService.getAllModules().stream().map(moduleService::convertToDTO).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleDTO> getModuleById(@PathVariable Long id) {
        return moduleService.getModuleById(id)
                .map(moduleService::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ModuleDTO createModule(@RequestBody Module module) {
        return moduleService.convertToDTO(moduleService.createModule(module));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModuleDTO> updateModule(@PathVariable Long id, @RequestBody Module details) {
        try {
            return ResponseEntity.ok(moduleService.convertToDTO(moduleService.updateModule(id, details)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        moduleService.deleteModule(id);
        return ResponseEntity.noContent().build();
    }
}
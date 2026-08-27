package com.qcm.backend.service;

import com.qcm.backend.dto.CreateModuleRequest;
import com.qcm.backend.dto.ModuleDTO;
import com.qcm.backend.entity.Module;
import com.qcm.backend.exception.ConflitException;
import com.qcm.backend.exception.RessourceNonTrouveeException;
import com.qcm.backend.repository.ModuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    public Module getModuleById(Long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Module non trouvé"));
    }

    public Module createModule(CreateModuleRequest request) {
        if (moduleRepository.existsByNom(request.getNom())) {
            throw new ConflitException("Un module avec ce nom existe déjà");
        }

        Module module = new Module();
        module.setNom(request.getNom());
        module.setDescription(request.getDescription());
        return moduleRepository.save(module);
    }

    public Module updateModule(Long id, Module details) {
        Module module = getModuleById(id);
        module.setNom(details.getNom());
        module.setDescription(details.getDescription());
        return moduleRepository.save(module);
    }

    public void deleteModule(Long id) {
        moduleRepository.deleteById(id);
    }

    public ModuleDTO convertToDTO(Module module) {
        ModuleDTO dto = new ModuleDTO();
        dto.setId(module.getId());
        dto.setNom(module.getNom());
        dto.setDescription(module.getDescription());
        return dto;
    }
}
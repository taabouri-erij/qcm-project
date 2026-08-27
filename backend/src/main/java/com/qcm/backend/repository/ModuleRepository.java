package com.qcm.backend.repository;

import com.qcm.backend.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<Module, Long> {
    boolean existsByNom(String nom);
}
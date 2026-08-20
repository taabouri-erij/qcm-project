package com.qcm.backend.service;

import com.qcm.backend.entity.AuditLog;
import com.qcm.backend.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public List<AuditLog> getAll() {
        return auditLogRepository.findAll();
    }

    public List<AuditLog> getByUtilisateur(Long utilisateurId) {
        return auditLogRepository.findByUtilisateurId(utilisateurId);
    }

    public AuditLog create(AuditLog log) {
        return auditLogRepository.save(log);
    }
}
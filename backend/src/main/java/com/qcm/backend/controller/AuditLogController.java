package com.qcm.backend.controller;

import com.qcm.backend.entity.AuditLog;
import com.qcm.backend.service.AuditLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@CrossOrigin(origins = "*")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public List<AuditLog> getAll() {
        return auditLogService.getAll();
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    public List<AuditLog> getByUtilisateur(@PathVariable Long utilisateurId) {
        return auditLogService.getByUtilisateur(utilisateurId);
    }
}
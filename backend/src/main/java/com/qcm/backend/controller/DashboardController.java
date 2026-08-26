package com.qcm.backend.controller;

import com.qcm.backend.service.DashboardService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // Tableau de bord global
    @GetMapping("/dashboard")
    public Map<String, Object> getDashboard() {
        return dashboardService.getDashboard();
    }

    // Statistiques globales
    @GetMapping("/statistiques")
    public Map<String, Object> getStatistiques() {
        return dashboardService.getStatistiques();
    }
}
package com.citypulse.presentation.controller;

import com.citypulse.application.dto.response.DashboardResponse;
import com.citypulse.application.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public Mono<DashboardResponse> getDashboard() {
        return dashboardService.getDashboard();
    }
}

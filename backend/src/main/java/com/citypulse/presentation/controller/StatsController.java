package com.citypulse.presentation.controller;

import com.citypulse.application.dto.response.StatsResponse;
import com.citypulse.application.service.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping
    public Mono<StatsResponse> getStats() {
        return statsService.getStats();
    }
}

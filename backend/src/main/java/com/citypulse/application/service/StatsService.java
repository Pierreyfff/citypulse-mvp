package com.citypulse.application.service;

import com.citypulse.application.dto.response.StatsResponse;
import com.citypulse.domain.enums.IncidentStatus;
import com.citypulse.domain.enums.IncidentType;
import com.citypulse.infrastructure.persistence.repository.ReactiveIncidentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StatsService {

    private final ReactiveIncidentRepository incidentRepository;

    public StatsService(ReactiveIncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public Mono<StatsResponse> getStats() {
        return incidentRepository.findAll()
                .collectList()
                .map(incidents -> {
                    Map<String, Long> byType = incidents.stream()
                            .collect(Collectors.groupingBy(
                                    i -> i.getType().name(),
                                    Collectors.counting()
                            ));

                    Map<String, Long> byStatus = incidents.stream()
                            .collect(Collectors.groupingBy(
                                    i -> i.getStatus().name(),
                                    Collectors.counting()
                            ));

                    Map<String, Long> byDay = incidents.stream()
                            .filter(i -> i.getCreatedAt() != null)
                            .collect(Collectors.groupingBy(
                                    i -> i.getCreatedAt().toLocalDate().toString(),
                                    Collectors.counting()
                            ));

                    Map<String, Long> byMonth = incidents.stream()
                            .filter(i -> i.getCreatedAt() != null)
                            .collect(Collectors.groupingBy(
                                    i -> i.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                                    Collectors.counting()
                            ));

                    return new StatsResponse(byType, byStatus, byDay, byMonth);
                });
    }
}

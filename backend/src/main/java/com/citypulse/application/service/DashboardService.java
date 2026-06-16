package com.citypulse.application.service;

import com.citypulse.application.dto.response.DashboardResponse;
import com.citypulse.application.mapper.DashboardMapper;
import com.citypulse.domain.enums.IncidentStatus;
import com.citypulse.infrastructure.persistence.repository.ReactiveIncidentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
public class DashboardService {

    private final ReactiveIncidentRepository incidentRepository;
    private final DashboardMapper dashboardMapper;

    public DashboardService(ReactiveIncidentRepository incidentRepository,
                            DashboardMapper dashboardMapper) {
        this.incidentRepository = incidentRepository;
        this.dashboardMapper = dashboardMapper;
    }

    public Mono<DashboardResponse> getDashboard() {
        Function<IncidentStatus, Mono<Long>> countByStatus = status ->
                incidentRepository.countByStatus(status);

        return Mono.zip(
                incidentRepository.count(),
                countByStatus.apply(IncidentStatus.REPORTADO),
                countByStatus.apply(IncidentStatus.RESUELTO),
                incidentRepository.findByStatus(IncidentStatus.REPORTADO)
                        .filter(incident -> incident.getRiskScore() >= 70)
                        .count()
        ).map(tuple -> dashboardMapper.toResponse(
                tuple.getT1(),
                tuple.getT2(),
                tuple.getT3(),
                tuple.getT4()
        ));
    }
}

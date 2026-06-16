package com.citypulse.application.service;

import com.citypulse.application.dto.request.IncidentRequest;
import com.citypulse.application.dto.response.IncidentResponse;
import com.citypulse.application.mapper.IncidentMapper;
import com.citypulse.domain.enums.IncidentStatus;
import com.citypulse.domain.enums.IncidentType;
import com.citypulse.domain.exception.ResourceNotFoundException;
import com.citypulse.domain.model.Incident;
import com.citypulse.infrastructure.persistence.mapper.IncidentEntityMapper;
import com.citypulse.infrastructure.persistence.repository.ReactiveIncidentRepository;
import com.citypulse.infrastructure.websocket.IncidentEventBus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class IncidentService {

    private final ReactiveIncidentRepository incidentRepository;
    private final IncidentMapper incidentMapper;
    private final IncidentEntityMapper entityMapper;
    private final RiskEngine riskEngine;
    private final IncidentEventBus eventBus;

    public IncidentService(ReactiveIncidentRepository incidentRepository,
                           IncidentMapper incidentMapper,
                           IncidentEntityMapper entityMapper,
                           RiskEngine riskEngine,
                           IncidentEventBus eventBus) {
        this.incidentRepository = incidentRepository;
        this.incidentMapper = incidentMapper;
        this.entityMapper = entityMapper;
        this.riskEngine = riskEngine;
        this.eventBus = eventBus;
    }

    public Flux<IncidentResponse> findAll() {
        return incidentRepository.findAll()
                .map(entityMapper::toDomain)
                .map(incidentMapper::toResponse);
    }

    public Flux<IncidentResponse> findByStatus(IncidentStatus status) {
        return incidentRepository.findByStatus(status)
                .map(entityMapper::toDomain)
                .map(incidentMapper::toResponse);
    }

    public Flux<IncidentResponse> findByType(IncidentType type) {
        return incidentRepository.findByType(type)
                .map(entityMapper::toDomain)
                .map(incidentMapper::toResponse);
    }

    public Mono<IncidentResponse> findById(Long id) {
        return incidentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Incidente", id)))
                .map(entityMapper::toDomain)
                .map(incidentMapper::toResponse);
    }

    public Flux<IncidentResponse> findByUserId(Long userId) {
        return incidentRepository.findByUserId(userId)
                .map(entityMapper::toDomain)
                .map(incidentMapper::toResponse);
    }

    public Mono<IncidentResponse> create(IncidentRequest request, Long userId) {
        return Mono.just(request)
                .map(req -> incidentMapper.toDomain(req, userId))
                .map(incident -> {
                    int riskScore = riskEngine.calculateRisk(incident);
                    return Incident.builder()
                            .type(incident.getType())
                            .status(IncidentStatus.REPORTADO)
                            .description(incident.getDescription())
                            .address(incident.getAddress())
                            .latitude(incident.getLatitude())
                            .longitude(incident.getLongitude())
                            .severity(incident.getSeverity())
                            .sensitiveZone(incident.isSensitiveZone())
                            .riskScore(riskScore)
                            .userId(incident.getUserId())
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                })
                .map(entityMapper::toEntity)
                .flatMap(incidentRepository::save)
                .map(entityMapper::toDomain)
                .flatMap(incident -> eventBus.emitCreated(incident)
                        .thenReturn(incident))
                .map(incidentMapper::toResponse);
    }

    public Mono<IncidentResponse> updateStatus(Long id, IncidentStatus newStatus) {
        return incidentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Incidente", id)))
                .flatMap(entity -> {
                    entity.setStatus(newStatus);
                    entity.setUpdatedAt(LocalDateTime.now());
                    return incidentRepository.save(entity);
                })
                .map(entityMapper::toDomain)
                .flatMap(incident -> {
                    if (newStatus == IncidentStatus.RESUELTO) {
                        return eventBus.emitResolved(incident).thenReturn(incident);
                    }
                    return eventBus.emitUpdated(incident).thenReturn(incident);
                })
                .map(incidentMapper::toResponse);
    }

    public Mono<IncidentResponse> update(Long id, IncidentRequest request) {
        return incidentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Incidente", id)))
                .flatMap(existing -> {
                    existing.setType(request.type());
                    existing.setDescription(request.description());
                    existing.setAddress(request.address());
                    existing.setLatitude(request.latitude());
                    existing.setLongitude(request.longitude());
                    existing.setSeverity(request.severity().getMaxValue());
                    existing.setSensitiveZone(request.sensitiveZone() != null && request.sensitiveZone());
                    existing.setUpdatedAt(LocalDateTime.now());

                    var domain = entityMapper.toDomain(existing);
                    int riskScore = riskEngine.calculateRisk(domain);
                    existing.setRiskScore(riskScore);

                    return incidentRepository.save(existing);
                })
                .map(entityMapper::toDomain)
                .flatMap(eventBus::emitUpdated)
                .map(incidentMapper::toResponse);
    }

    public Mono<Void> delete(Long id) {
        return incidentRepository.deleteById(id);
    }
}

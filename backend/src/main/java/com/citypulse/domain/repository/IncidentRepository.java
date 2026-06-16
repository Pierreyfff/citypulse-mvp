package com.citypulse.domain.repository;

import com.citypulse.domain.enums.IncidentStatus;
import com.citypulse.domain.enums.IncidentType;
import com.citypulse.domain.model.Incident;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface IncidentRepository {
    Mono<Incident> findById(Long id);
    Flux<Incident> findAll();
    Flux<Incident> findByStatus(IncidentStatus status);
    Flux<Incident> findByType(IncidentType type);
    Flux<Incident> findByUserId(Long userId);
    Mono<Incident> save(Incident incident);
    Mono<Void> deleteById(Long id);
    Mono<Long> count();
    Mono<Long> countByStatus(IncidentStatus status);
    Flux<Incident> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}

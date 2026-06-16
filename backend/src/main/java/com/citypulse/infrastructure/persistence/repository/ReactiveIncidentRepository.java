package com.citypulse.infrastructure.persistence.repository;

import com.citypulse.domain.enums.IncidentStatus;
import com.citypulse.domain.enums.IncidentType;
import com.citypulse.infrastructure.persistence.entity.IncidentEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public interface ReactiveIncidentRepository extends R2dbcRepository<IncidentEntity, Long> {
    Flux<IncidentEntity> findByStatus(IncidentStatus status);
    Flux<IncidentEntity> findByType(IncidentType type);
    Flux<IncidentEntity> findByUserId(Long userId);
    Mono<Long> countByStatus(IncidentStatus status);
    Flux<IncidentEntity> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}

package com.citypulse.infrastructure.persistence.repository;

import com.citypulse.infrastructure.persistence.entity.RefreshTokenEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveRefreshTokenRepository extends R2dbcRepository<RefreshTokenEntity, Long> {
    Mono<RefreshTokenEntity> findByToken(String token);
    Flux<RefreshTokenEntity> findByUserId(Long userId);
    Mono<Void> deleteByUserId(Long userId);
}

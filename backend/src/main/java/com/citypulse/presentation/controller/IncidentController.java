package com.citypulse.presentation.controller;

import com.citypulse.application.dto.request.IncidentRequest;
import com.citypulse.application.dto.response.IncidentResponse;
import com.citypulse.application.service.IncidentService;
import com.citypulse.domain.enums.IncidentStatus;
import com.citypulse.domain.enums.IncidentType;
import com.citypulse.domain.exception.ResourceNotFoundException;
import com.citypulse.domain.exception.UnauthorizedException;
import com.citypulse.infrastructure.persistence.entity.UserEntity;
import com.citypulse.infrastructure.persistence.repository.ReactiveUserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidentService;
    private final ReactiveUserRepository userRepository;

    public IncidentController(IncidentService incidentService,
                              ReactiveUserRepository userRepository) {
        this.incidentService = incidentService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public Mono<List<IncidentResponse>> findAll(
            @RequestParam(required = false) IncidentStatus status,
            @RequestParam(required = false) IncidentType type,
            Authentication auth) {
        return extractUserAndRole(auth)
                .flatMap(tuple -> {
                    UserEntity user = tuple.getT1();
                    String role = tuple.getT2();
                    if ("ROLE_CIUDADANO".equals(role)) {
                        return incidentService.findByUserId(user.getId()).collectList();
                    }
                    Flux<IncidentResponse> flux = Optional.ofNullable(status)
                            .map(incidentService::findByStatus)
                            .orElseGet(() -> Optional.ofNullable(type)
                                    .map(incidentService::findByType)
                                    .orElseGet(incidentService::findAll));
                    return flux.collectList();
                });
    }

    @GetMapping("/{id}")
    public Mono<IncidentResponse> findById(@PathVariable Long id, Authentication auth) {
        return extractUserAndRole(auth)
                .flatMap(tuple -> {
                    String role = tuple.getT2();
                    return incidentService.findById(id)
                            .flatMap(incident -> {
                                if ("ROLE_CIUDADANO".equals(role) && !incident.userId().equals(tuple.getT1().getId())) {
                                    return Mono.error(new ResourceNotFoundException("Incidente", id));
                                }
                                return Mono.just(incident);
                            });
                });
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<IncidentResponse> create(@Valid @RequestBody IncidentRequest request,
                                          Authentication auth) {
        return extractUserId(auth)
                .flatMap(userId -> incidentService.create(request, userId));
    }

    @PutMapping("/{id}")
    public Mono<IncidentResponse> update(@PathVariable Long id,
                                          @Valid @RequestBody IncidentRequest request,
                                          Authentication auth) {
        return extractUserAndRole(auth)
                .flatMap(tuple -> {
                    String role = tuple.getT2();
                    Long userId = tuple.getT1().getId();
                    if ("ROLE_CIUDADANO".equals(role)) {
                        return incidentService.findById(id)
                                .flatMap(incident -> {
                                    if (!incident.userId().equals(userId)) {
                                        return Mono.error(new UnauthorizedException("No puedes editar este incidente"));
                                    }
                                    return incidentService.update(id, request);
                                });
                    }
                    return incidentService.update(id, request);
                });
    }

    @PatchMapping("/{id}/status")
    public Mono<IncidentResponse> updateStatus(@PathVariable Long id,
                                                @RequestParam IncidentStatus status,
                                                Authentication auth) {
        return extractUserAndRole(auth)
                .flatMap(tuple -> {
                    String role = tuple.getT2();
                    if ("ROLE_CIUDADANO".equals(role)) {
                        return Mono.error(new UnauthorizedException("No puedes cambiar el estado"));
                    }
                    return incidentService.updateStatus(id, status);
                });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return incidentService.delete(id);
    }

    private Mono<Long> extractUserId(Authentication auth) {
        return userRepository.findByUsername(auth.getName())
                .map(UserEntity::getId);
    }

    private Mono<Tuple2<UserEntity, String>> extractUserAndRole(Authentication auth) {
        return userRepository.findByUsername(auth.getName())
                .map(user -> Tuples.of(user, auth.getAuthorities().iterator().next().getAuthority()));
    }
}

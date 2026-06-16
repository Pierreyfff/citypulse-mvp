package com.citypulse.application.service;

import com.citypulse.application.dto.request.IncidentRequest;
import com.citypulse.application.dto.response.IncidentResponse;
import com.citypulse.application.mapper.IncidentMapper;
import com.citypulse.domain.enums.IncidentStatus;
import com.citypulse.domain.enums.IncidentType;
import com.citypulse.domain.enums.SeverityLevel;
import com.citypulse.domain.exception.ResourceNotFoundException;
import com.citypulse.domain.model.Incident;
import com.citypulse.infrastructure.persistence.entity.IncidentEntity;
import com.citypulse.infrastructure.persistence.mapper.IncidentEntityMapper;
import com.citypulse.infrastructure.persistence.repository.ReactiveIncidentRepository;
import com.citypulse.infrastructure.websocket.IncidentEventBus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("IncidentService - Servicio de incidentes")
class IncidentServiceTest {

    @Mock
    private ReactiveIncidentRepository incidentRepository;

    @Mock
    private IncidentMapper incidentMapper;

    @Mock
    private IncidentEntityMapper entityMapper;

    @Mock
    private RiskEngine riskEngine;

    @Mock
    private IncidentEventBus eventBus;

    @InjectMocks
    private IncidentService incidentService;

    @Captor
    private ArgumentCaptor<IncidentEntity> entityCaptor;

    private final IncidentEntity sampleEntity = createSampleEntity();
    private final Incident sampleDomain = createSampleDomain();
    private final IncidentResponse sampleResponse = createSampleResponse();

    @Nested
    @DisplayName("findAll()")
    class FindAll {

        @Test
        @DisplayName("retorna lista de incidentes cuando existen")
        void returnsIncidents() {
            when(incidentRepository.findAll()).thenReturn(Flux.just(sampleEntity));
            when(entityMapper.toDomain(any())).thenReturn(sampleDomain);
            when(incidentMapper.toResponse(any())).thenReturn(sampleResponse);

            incidentService.findAll()
                    .as(StepVerifier::create)
                    .expectNext(sampleResponse)
                    .verifyComplete();

            verify(incidentRepository).findAll();
        }

        @Test
        @DisplayName("retorna vacio cuando no hay incidentes")
        void returnsEmptyWhenNoIncidents() {
            when(incidentRepository.findAll()).thenReturn(Flux.empty());

            incidentService.findAll()
                    .as(StepVerifier::create)
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindById {

        @Test
        @DisplayName("retorna incidente cuando existe")
        void returnsIncidentWhenFound() {
            when(incidentRepository.findById(anyLong())).thenReturn(Mono.just(sampleEntity));
            when(entityMapper.toDomain(any())).thenReturn(sampleDomain);
            when(incidentMapper.toResponse(any())).thenReturn(sampleResponse);

            incidentService.findById(1L)
                    .as(StepVerifier::create)
                    .expectNext(sampleResponse)
                    .verifyComplete();
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando no existe")
        void throwsWhenNotFound() {
            when(incidentRepository.findById(anyLong())).thenReturn(Mono.empty());

            incidentService.findById(999L)
                    .as(StepVerifier::create)
                    .expectError(ResourceNotFoundException.class)
                    .verify();
        }
    }

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("crea incidente y emite evento")
        void createsAndEmitsEvent() {
            var request = new IncidentRequest(
                    IncidentType.INCENDIO, "Incendio en Palermo",
                    "Av. Libertador 123", -34.5, -58.4,
                    SeverityLevel.CRITICO, true
            );

            when(incidentMapper.toDomain(any(), anyLong())).thenReturn(createSampleDomain());
            when(riskEngine.calculateRisk(any())).thenReturn(80);
            when(entityMapper.toEntity(any())).thenReturn(sampleEntity);
            when(incidentRepository.save(any())).thenReturn(Mono.just(sampleEntity));
            when(entityMapper.toDomain(any())).thenReturn(sampleDomain);
            when(eventBus.emitCreated(any())).thenReturn(Mono.just(sampleDomain));
            when(incidentMapper.toResponse(any())).thenReturn(sampleResponse);

            incidentService.create(request, 1L)
                    .as(StepVerifier::create)
                    .expectNext(sampleResponse)
                    .verifyComplete();

            verify(riskEngine).calculateRisk(any());
            verify(eventBus).emitCreated(any());
        }
    }

    @Nested
    @DisplayName("updateStatus()")
    class UpdateStatus {

        @Test
        @DisplayName("actualiza estado a RESUELTO y emite evento de resolucion")
        void updatesToResolved() {
            var entity = sampleEntity;
            entity.setStatus(IncidentStatus.REPORTADO);

            when(incidentRepository.findById(anyLong())).thenReturn(Mono.just(entity));
            when(incidentRepository.save(any())).thenReturn(Mono.just(entity));
            when(entityMapper.toDomain(any())).thenReturn(sampleDomain);
            when(eventBus.emitResolved(any())).thenReturn(Mono.just(sampleDomain));
            when(incidentMapper.toResponse(any())).thenReturn(sampleResponse);

            incidentService.updateStatus(1L, IncidentStatus.RESUELTO)
                    .as(StepVerifier::create)
                    .expectNext(sampleResponse)
                    .verifyComplete();

            verify(eventBus).emitResolved(any());
        }

        @Test
        @DisplayName("actualiza estado a EN_PROCESO y emite evento de actualizacion")
        void updatesToInProgress() {
            var entity = sampleEntity;
            entity.setStatus(IncidentStatus.REPORTADO);

            when(incidentRepository.findById(anyLong())).thenReturn(Mono.just(entity));
            when(incidentRepository.save(any())).thenReturn(Mono.just(entity));
            when(entityMapper.toDomain(any())).thenReturn(sampleDomain);
            when(eventBus.emitUpdated(any())).thenReturn(Mono.just(sampleDomain));
            when(incidentMapper.toResponse(any())).thenReturn(sampleResponse);

            incidentService.updateStatus(1L, IncidentStatus.EN_PROCESO)
                    .as(StepVerifier::create)
                    .expectNext(sampleResponse)
                    .verifyComplete();

            verify(eventBus).emitUpdated(any());
        }
    }

    @Nested
    @DisplayName("delete()")
    class Delete {

        @Test
        @DisplayName("elimina incidente cuando existe")
        void deletesWhenFound() {
            when(incidentRepository.findById(anyLong())).thenReturn(Mono.just(sampleEntity));
            when(incidentRepository.delete(any())).thenReturn(Mono.empty());

            incidentService.delete(1L)
                    .as(StepVerifier::create)
                    .verifyComplete();
        }

        @Test
        @DisplayName("lanza ResourceNotFoundException cuando no existe")
        void throwsWhenNotFound() {
            when(incidentRepository.findById(anyLong())).thenReturn(Mono.empty());

            incidentService.delete(999L)
                    .as(StepVerifier::create)
                    .expectError(ResourceNotFoundException.class)
                    .verify();
        }
    }

    @Nested
    @DisplayName("Demostracion de Programacion Reactiva")
    class ReactiveProgrammingDemo {

        @Test
        @DisplayName("uso de flatMap para composicion reactiva")
        void reactiveFlatMap() {
            when(incidentRepository.findById(anyLong())).thenReturn(Mono.just(sampleEntity));
            when(entityMapper.toDomain(any())).thenReturn(sampleDomain);
            when(incidentMapper.toResponse(any())).thenReturn(sampleResponse);

            incidentService.findById(1L)
                    .map(IncidentResponse::description)
                    .as(StepVerifier::create)
                    .expectNext("Incendio en Palermo")
                    .verifyComplete();
        }
    }

    private IncidentEntity createSampleEntity() {
        var entity = new IncidentEntity();
        entity.setId(1L);
        entity.setType(IncidentType.INCENDIO);
        entity.setStatus(IncidentStatus.REPORTADO);
        entity.setDescription("Incendio en Palermo");
        entity.setAddress("Av. Libertador 123");
        entity.setLatitude(-34.5);
        entity.setLongitude(-58.4);
        entity.setSeverity(80);
        entity.setSensitiveZone(true);
        entity.setRiskScore(80);
        entity.setUserId(1L);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    private Incident createSampleDomain() {
        return Incident.builder()
                .id(1L)
                .type(IncidentType.INCENDIO)
                .status(IncidentStatus.REPORTADO)
                .description("Incendio en Palermo")
                .address("Av. Libertador 123")
                .latitude(-34.5)
                .longitude(-58.4)
                .severity(80)
                .sensitiveZone(true)
                .riskScore(80)
                .userId(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private IncidentResponse createSampleResponse() {
        return new IncidentResponse(
                1L, IncidentType.INCENDIO, IncidentStatus.REPORTADO,
                "Incendio en Palermo", "Av. Libertador 123",
                -34.5, -58.4, 80, SeverityLevel.CRITICO, true, 80,
                1L, LocalDateTime.now(), LocalDateTime.now()
        );
    }
}

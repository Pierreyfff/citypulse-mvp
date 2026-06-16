package com.citypulse.infrastructure.websocket;

import com.citypulse.domain.model.Incident;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
public class IncidentEventBus {

    private final Sinks.Many<IncidentEvent> sink;

    public IncidentEventBus() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public Mono<Incident> emitCreated(Incident incident) {
        return Mono.fromRunnable(() -> {
            var event = new IncidentEvent("INCIDENTE_CREADO", incident);
            Sinks.EmitResult result = sink.tryEmitNext(event);
            if (result.isFailure()) {
                sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
            }
        }).thenReturn(incident);
    }

    public Mono<Incident> emitUpdated(Incident incident) {
        return Mono.fromRunnable(() -> {
            var event = new IncidentEvent("INCIDENTE_ACTUALIZADO", incident);
            Sinks.EmitResult result = sink.tryEmitNext(event);
            if (result.isFailure()) {
                sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
            }
        }).thenReturn(incident);
    }

    public Mono<Incident> emitResolved(Incident incident) {
        return Mono.fromRunnable(() -> {
            var event = new IncidentEvent("INCIDENTE_RESUELTO", incident);
            Sinks.EmitResult result = sink.tryEmitNext(event);
            if (result.isFailure()) {
                sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
            }
        }).thenReturn(incident);
    }

    public Flux<IncidentEvent> getEventStream() {
        return sink.asFlux();
    }

    public record IncidentEvent(String type, Incident incident) {}
}

package com.citypulse.infrastructure.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.Map;

@Configuration
public class WebSocketConfig {

    private final IncidentEventBus eventBus;
    private final ObjectMapper objectMapper;

    public WebSocketConfig(IncidentEventBus eventBus, ObjectMapper objectMapper) {
        this.eventBus = eventBus;
        this.objectMapper = objectMapper;
    }

    @Bean
    public HandlerMapping webSocketHandlerMapping() {
        Map<String, WebSocketHandler> map = Map.of(
                "/ws/incidents", this::incidentsHandler
        );
        return new SimpleUrlHandlerMapping(map, -1);
    }

    private Mono<Void> incidentsHandler(WebSocketSession session) {
        var eventStream = eventBus.getEventStream()
                .map(event -> {
                    try {
                        return objectMapper.writeValueAsString(event);
                    } catch (JsonProcessingException e) {
                        return "{}";
                    }
                })
                .map(session::textMessage);

        return session.send(eventStream);
    }
}

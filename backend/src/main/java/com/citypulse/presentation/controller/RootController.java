package com.citypulse.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Controller
public class RootController {

    @GetMapping("/")
    public Mono<Void> redirectToSwagger(ServerWebExchange exchange) {
        return Mono.fromRunnable(() -> {
            var response = exchange.getResponse();
            response.setStatusCode(org.springframework.http.HttpStatus.FOUND);
            response.getHeaders().set(org.springframework.http.HttpHeaders.LOCATION, "/swagger-ui.html");
        });
    }
}

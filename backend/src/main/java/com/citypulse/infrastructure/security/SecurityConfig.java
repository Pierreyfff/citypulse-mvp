package com.citypulse.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
                return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/auth/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/incidents/**").authenticated()
                        .pathMatchers(HttpMethod.POST, "/api/incidents").authenticated()
                        .pathMatchers(HttpMethod.PUT, "/api/incidents/**").authenticated()
                        .pathMatchers(HttpMethod.PATCH, "/api/incidents/*/status").hasAnyRole("ADMIN", "OPERADOR")
                        .pathMatchers(HttpMethod.DELETE, "/api/incidents/**").hasRole("ADMIN")
                        .pathMatchers("/api/users/**").hasRole("ADMIN")
                        .pathMatchers("/api/dashboard/**").authenticated()
                        .pathMatchers("/api/stats/**").hasAnyRole("ADMIN", "OPERADOR")
                        .pathMatchers("/ws/**").permitAll()
                        .pathMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

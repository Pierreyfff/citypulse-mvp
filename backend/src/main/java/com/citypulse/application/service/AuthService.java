package com.citypulse.application.service;

import com.citypulse.application.dto.request.LoginRequest;
import com.citypulse.application.dto.request.RefreshTokenRequest;
import com.citypulse.application.dto.request.RegisterRequest;
import com.citypulse.application.dto.response.AuthResponse;
import com.citypulse.domain.enums.Role;
import com.citypulse.domain.exception.UnauthorizedException;
import com.citypulse.domain.model.RefreshToken;
import com.citypulse.domain.model.User;
import com.citypulse.domain.repository.UserRepository;
import com.citypulse.infrastructure.persistence.mapper.UserEntityMapper;
import com.citypulse.infrastructure.persistence.repository.ReactiveRefreshTokenRepository;
import com.citypulse.infrastructure.persistence.repository.ReactiveUserRepository;
import com.citypulse.infrastructure.security.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final ReactiveUserRepository userRepository;
    private final ReactiveRefreshTokenRepository refreshTokenRepository;
    private final UserEntityMapper entityMapper;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(ReactiveUserRepository userRepository,
                       ReactiveRefreshTokenRepository refreshTokenRepository,
                       UserEntityMapper entityMapper,
                       JwtProvider jwtProvider,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.entityMapper = entityMapper;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<AuthResponse> register(RegisterRequest request) {
        return Mono.just(request)
                .flatMap(this::validateUniqueUser)
                .map(req -> User.builder()
                        .username(req.username())
                        .email(req.email())
                        .password(passwordEncoder.encode(req.password()))
                        .role(Optional.ofNullable(req.role()).orElse(Role.CIUDADANO))
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build())
                .map(entityMapper::toEntity)
                .flatMap(userRepository::save)
                .map(entityMapper::toDomain)
                .flatMap(this::generateAuthResponse);
    }

    public Mono<AuthResponse> login(LoginRequest request) {
        return userRepository.findByUsername(request.username())
                .switchIfEmpty(Mono.error(new UnauthorizedException("Credenciales invalidas")))
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .switchIfEmpty(Mono.error(new UnauthorizedException("Credenciales invalidas")))
                .filter(com.citypulse.infrastructure.persistence.entity.UserEntity::isActive)
                .switchIfEmpty(Mono.error(new UnauthorizedException("Usuario inactivo")))
                .map(entityMapper::toDomain)
                .flatMap(this::generateAuthResponse);
    }

    public Mono<AuthResponse> refresh(RefreshTokenRequest request) {
        return refreshTokenRepository.findByToken(request.refreshToken())
                .switchIfEmpty(Mono.error(new UnauthorizedException("Refresh token invalido")))
                .filter(token -> token.getExpiresAt().isAfter(LocalDateTime.now()))
                .switchIfEmpty(Mono.error(new UnauthorizedException("Refresh token expirado")))
                .flatMap(token -> userRepository.findById(token.getUserId())
                        .switchIfEmpty(Mono.error(new UnauthorizedException("Usuario no encontrado")))
                        .map(entityMapper::toDomain)
                        .flatMap(this::generateAuthResponse));
    }

    private Mono<AuthResponse> generateAuthResponse(User user) {
        String token = jwtProvider.generateToken(user.getUsername(), user.getRole().name());
        String refreshTokenValue = jwtProvider.generateRefreshToken(user.getUsername());

        var refreshEntity = new com.citypulse.infrastructure.persistence.entity.RefreshTokenEntity();
        refreshEntity.setToken(refreshTokenValue);
        refreshEntity.setUserId(user.getId());
        refreshEntity.setExpiresAt(LocalDateTime.now().plusDays(1));
        refreshEntity.setCreatedAt(LocalDateTime.now());

        return refreshTokenRepository.save(refreshEntity)
                .thenReturn(new AuthResponse(
                        token, refreshTokenValue,
                        user.getUsername(), user.getEmail(), user.getRole()
                ));
    }

    private Mono<RegisterRequest> validateUniqueUser(RegisterRequest request) {
        return userRepository.findByUsername(request.username())
                .flatMap(existing -> Mono.<RegisterRequest>error(
                        new IllegalArgumentException("Username ya registrado")))
                .switchIfEmpty(
                        userRepository.findByEmail(request.email())
                                .flatMap(existing -> Mono.<RegisterRequest>error(
                                        new IllegalArgumentException("Email ya registrado")))
                                .switchIfEmpty(Mono.just(request))
                );
    }
}

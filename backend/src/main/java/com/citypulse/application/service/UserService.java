package com.citypulse.application.service;

import com.citypulse.application.dto.request.UserRequest;
import com.citypulse.application.dto.response.UserResponse;
import com.citypulse.application.mapper.UserMapper;
import com.citypulse.domain.exception.ResourceNotFoundException;
import com.citypulse.domain.model.User;
import com.citypulse.infrastructure.persistence.mapper.UserEntityMapper;
import com.citypulse.infrastructure.persistence.repository.ReactiveUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.function.Function;

@Service
public class UserService {

    private final ReactiveUserRepository userRepository;
    private final UserMapper userMapper;
    private final UserEntityMapper entityMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(ReactiveUserRepository userRepository,
                       UserMapper userMapper,
                       UserEntityMapper entityMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.entityMapper = entityMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Flux<UserResponse> findAll() {
        return userRepository.findAll()
                .map(entityMapper::toDomain)
                .map(userMapper::toResponse);
    }

    public Mono<UserResponse> findById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Usuario", id)))
                .map(entityMapper::toDomain)
                .map(userMapper::toResponse);
    }

    public Mono<UserResponse> create(UserRequest request) {
        return Mono.just(request)
                .map(userMapper::toDomain)
                .map(user -> User.builder()
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .password(passwordEncoder.encode(user.getPassword()))
                        .role(user.getRole())
                        .active(user.isActive())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build())
                .map(entityMapper::toEntity)
                .flatMap(userRepository::save)
                .map(entityMapper::toDomain)
                .map(userMapper::toResponse);
    }

    public Mono<UserResponse> update(Long id, UserRequest request) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Usuario", id)))
                .flatMap(existing -> {
                    var entity = entityMapper.toEntity(
                            User.builder()
                                    .id(id)
                                    .username(request.username())
                                    .email(request.email())
                                    .password(request.password() != null && !request.password().isBlank()
                                            ? passwordEncoder.encode(request.password())
                                            : existing.getPassword())
                                    .role(request.role() != null ? request.role() : existing.getRole())
                                    .active(request.active() != null ? request.active() : existing.isActive())
                                    .createdAt(existing.getCreatedAt())
                                    .updatedAt(LocalDateTime.now())
                                    .build()
                    );
                    return userRepository.save(entity);
                })
                .map(entityMapper::toDomain)
                .map(userMapper::toResponse);
    }

    public Mono<Void> delete(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Usuario", id)))
                .flatMap(userRepository::delete);
    }
}

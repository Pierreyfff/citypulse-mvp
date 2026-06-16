package com.citypulse.infrastructure.security;

import com.citypulse.infrastructure.persistence.entity.UserEntity;
import com.citypulse.infrastructure.persistence.repository.ReactiveUserRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final ReactiveUserRepository userRepository;

    public ReactiveUserDetailsServiceImpl(ReactiveUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .filter(UserEntity::isActive)
                .map(this::toUserDetails);
    }

    private UserDetails toUserDetails(UserEntity entity) {
        return new User(
                entity.getUsername(),
                entity.getPassword(),
                Collections.singletonList(() -> "ROLE_" + entity.getRole().name())
        );
    }
}

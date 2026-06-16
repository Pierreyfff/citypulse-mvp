package com.citypulse.presentation.config;

import com.citypulse.domain.enums.Role;
import com.citypulse.infrastructure.persistence.entity.UserEntity;
import com.citypulse.infrastructure.persistence.repository.ReactiveUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Profile("!test")
@Component
public class DataInitializer implements CommandLineRunner {

    private final ReactiveUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(ReactiveUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        var users = List.of(
                createUser("admin", "admin@citypulse.com", "admin123", Role.ADMIN),
                createUser("operador1", "operador1@citypulse.com", "operador123", Role.OPERADOR),
                createUser("ciudadano1", "ciudadano1@citypulse.com", "ciudadano123", Role.CIUDADANO)
        );

        Flux.fromIterable(users)
                .flatMap(user -> userRepository.findByUsername(user.getUsername())
                        .hasElement()
                        .flatMap(exists -> exists
                                ? Mono.empty()
                                : userRepository.save(user)))
                .subscribe();
    }

    private UserEntity createUser(String username, String email, String password, Role role) {
        var user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
}

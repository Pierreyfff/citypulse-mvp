package com.citypulse.domain.model;

import com.citypulse.domain.enums.Role;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private final Long id;
    private final String username;
    private final String email;
    private final String password;
    private final Role role;
    private final boolean active;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private User(Builder builder) {
        this.id = builder.id;
        this.username = Objects.requireNonNull(builder.username, "username no puede ser nulo");
        this.email = Objects.requireNonNull(builder.email, "email no puede ser nulo");
        this.password = Objects.requireNonNull(builder.password, "password no puede ser nulo");
        this.role = Objects.requireNonNull(builder.role, "role no puede ser nulo");
        this.active = builder.active;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public static class Builder {
        private Long id;
        private String username;
        private String email;
        private String password;
        private Role role;
        private boolean active = true;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder role(Role role) { this.role = role; return this; }
        public Builder active(boolean active) { this.active = active; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public User build() { return new User(this); }
    }
}

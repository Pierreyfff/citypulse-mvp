package com.citypulse.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class RefreshToken {
    private final Long id;
    private final String token;
    private final Long userId;
    private final LocalDateTime expiresAt;
    private final LocalDateTime createdAt;

    private RefreshToken(Builder builder) {
        this.id = builder.id;
        this.token = Objects.requireNonNull(builder.token, "token no puede ser nulo");
        this.userId = Objects.requireNonNull(builder.userId, "userId no puede ser nulo");
        this.expiresAt = Objects.requireNonNull(builder.expiresAt, "expiresAt no puede ser nulo");
        this.createdAt = builder.createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() { return id; }
    public String getToken() { return token; }
    public Long getUserId() { return userId; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public static class Builder {
        private Long id;
        private String token;
        private Long userId;
        private LocalDateTime expiresAt;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder token(String token) { this.token = token; return this; }
        public Builder userId(Long userId) { this.userId = userId; return this; }
        public Builder expiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public RefreshToken build() { return new RefreshToken(this); }
    }
}

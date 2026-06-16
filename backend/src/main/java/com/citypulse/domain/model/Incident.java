package com.citypulse.domain.model;

import com.citypulse.domain.enums.IncidentStatus;
import com.citypulse.domain.enums.IncidentType;

import java.time.LocalDateTime;
import java.util.Objects;

public class Incident {
    private final Long id;
    private final IncidentType type;
    private final IncidentStatus status;
    private final String description;
    private final String address;
    private final double latitude;
    private final double longitude;
    private final int severity;
    private final boolean sensitiveZone;
    private final int riskScore;
    private final Long userId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private Incident(Builder builder) {
        this.id = builder.id;
        this.type = Objects.requireNonNull(builder.type, "type no puede ser nulo");
        this.status = Objects.requireNonNull(builder.status, "status no puede ser nulo");
        this.description = Objects.requireNonNull(builder.description, "description no puede ser nulo");
        this.address = builder.address;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.severity = builder.severity;
        this.sensitiveZone = builder.sensitiveZone;
        this.riskScore = builder.riskScore;
        this.userId = Objects.requireNonNull(builder.userId, "userId no puede ser nulo");
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() { return id; }
    public IncidentType getType() { return type; }
    public IncidentStatus getStatus() { return status; }
    public String getDescription() { return description; }
    public String getAddress() { return address; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public int getSeverity() { return severity; }
    public boolean isSensitiveZone() { return sensitiveZone; }
    public int getRiskScore() { return riskScore; }
    public Long getUserId() { return userId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public static class Builder {
        private Long id;
        private IncidentType type;
        private IncidentStatus status = IncidentStatus.REPORTADO;
        private String description;
        private String address;
        private double latitude;
        private double longitude;
        private int severity;
        private boolean sensitiveZone;
        private int riskScore;
        private Long userId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder type(IncidentType type) { this.type = type; return this; }
        public Builder status(IncidentStatus status) { this.status = status; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder address(String address) { this.address = address; return this; }
        public Builder latitude(double latitude) { this.latitude = latitude; return this; }
        public Builder longitude(double longitude) { this.longitude = longitude; return this; }
        public Builder severity(int severity) { this.severity = severity; return this; }
        public Builder sensitiveZone(boolean sensitiveZone) { this.sensitiveZone = sensitiveZone; return this; }
        public Builder riskScore(int riskScore) { this.riskScore = riskScore; return this; }
        public Builder userId(Long userId) { this.userId = userId; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public Incident build() { return new Incident(this); }
    }
}

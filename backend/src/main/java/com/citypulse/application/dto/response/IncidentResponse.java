package com.citypulse.application.dto.response;

import com.citypulse.domain.enums.IncidentStatus;
import com.citypulse.domain.enums.IncidentType;
import com.citypulse.domain.enums.SeverityLevel;

import java.time.LocalDateTime;

public record IncidentResponse(
        Long id,
        IncidentType type,
        IncidentStatus status,
        String description,
        String address,
        double latitude,
        double longitude,
        int severity,
        SeverityLevel severityLevel,
        boolean sensitiveZone,
        int riskScore,
        Long userId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

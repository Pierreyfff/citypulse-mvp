package com.citypulse.application.dto.request;

import com.citypulse.domain.enums.IncidentType;
import com.citypulse.domain.enums.SeverityLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IncidentRequest(
        @NotNull IncidentType type,
        @NotBlank String description,
        String address,
        @NotNull Double latitude,
        @NotNull Double longitude,
        @NotNull SeverityLevel severity,
        Boolean sensitiveZone
) {}
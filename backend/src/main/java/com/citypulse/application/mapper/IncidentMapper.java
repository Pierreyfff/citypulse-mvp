package com.citypulse.application.mapper;

import com.citypulse.application.dto.request.IncidentRequest;
import com.citypulse.application.dto.response.IncidentResponse;
import com.citypulse.domain.enums.SeverityLevel;
import com.citypulse.domain.model.Incident;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class IncidentMapper {

    public Incident toDomain(IncidentRequest request, Long userId) {
        return Incident.builder()
                .type(request.type())
                .description(request.description())
                .address(request.address())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .severity(request.severity().getMaxValue())
                .sensitiveZone(Optional.ofNullable(request.sensitiveZone()).orElse(false))
                .userId(userId)
                .build();
    }

    public IncidentResponse toResponse(Incident incident) {
        return new IncidentResponse(
                incident.getId(),
                incident.getType(),
                incident.getStatus(),
                incident.getDescription(),
                incident.getAddress(),
                incident.getLatitude(),
                incident.getLongitude(),
                incident.getSeverity(),
                SeverityLevel.fromValue(incident.getSeverity()),
                incident.isSensitiveZone(),
                incident.getRiskScore(),
                incident.getUserId(),
                incident.getCreatedAt(),
                incident.getUpdatedAt()
        );
    }
}

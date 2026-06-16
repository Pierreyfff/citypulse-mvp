package com.citypulse.infrastructure.persistence.mapper;

import com.citypulse.domain.model.Incident;
import com.citypulse.infrastructure.persistence.entity.IncidentEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class IncidentEntityMapper {

    public IncidentEntity toEntity(Incident incident) {
        IncidentEntity entity = new IncidentEntity();
        Optional.ofNullable(incident.getId()).ifPresent(entity::setId);
        entity.setType(incident.getType());
        entity.setStatus(incident.getStatus());
        entity.setDescription(incident.getDescription());
        entity.setAddress(incident.getAddress());
        entity.setLatitude(incident.getLatitude());
        entity.setLongitude(incident.getLongitude());
        entity.setSeverity(incident.getSeverity());
        entity.setSensitiveZone(incident.isSensitiveZone());
        entity.setRiskScore(incident.getRiskScore());
        entity.setUserId(incident.getUserId());
        Optional.ofNullable(incident.getCreatedAt()).ifPresent(entity::setCreatedAt);
        Optional.ofNullable(incident.getUpdatedAt()).ifPresent(entity::setUpdatedAt);
        return entity;
    }

    public Incident toDomain(IncidentEntity entity) {
        return Incident.builder()
                .id(entity.getId())
                .type(entity.getType())
                .status(entity.getStatus())
                .description(entity.getDescription())
                .address(entity.getAddress())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .severity(entity.getSeverity())
                .sensitiveZone(entity.isSensitiveZone())
                .riskScore(entity.getRiskScore())
                .userId(entity.getUserId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

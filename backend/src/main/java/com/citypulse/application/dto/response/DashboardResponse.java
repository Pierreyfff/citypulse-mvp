package com.citypulse.application.dto.response;

public record DashboardResponse(
        long totalIncidents,
        long activeIncidents,
        long resolvedIncidents,
        long criticalIncidents
) {}

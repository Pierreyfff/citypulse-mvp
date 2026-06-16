package com.citypulse.application.dto.response;

import java.util.Map;

public record StatsResponse(
        Map<String, Long> incidentsByType,
        Map<String, Long> incidentsByStatus,
        Map<String, Long> incidentsByDay,
        Map<String, Long> incidentsByMonth
) {}

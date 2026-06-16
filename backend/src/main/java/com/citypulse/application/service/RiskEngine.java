package com.citypulse.application.service;

import com.citypulse.domain.enums.IncidentType;
import com.citypulse.domain.enums.SeverityLevel;
import com.citypulse.domain.model.Incident;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

@Component
public class RiskEngine {

    private static final List<Predicate<Incident>> riskFactors = List.of(
            incident -> {
                var level = SeverityLevel.fromValue(incident.getSeverity());
                return level == SeverityLevel.ALTO || level == SeverityLevel.CRITICO;
            },
            incident -> incident.getType() == IncidentType.INCENDIO,
            incident -> incident.getType() == IncidentType.ROBO,
            incident -> incident.isSensitiveZone(),
            incident -> SeverityLevel.fromValue(incident.getSeverity()) != SeverityLevel.BAJO
                    && incident.isSensitiveZone()
    );

    private static final ToIntFunction<Predicate<Incident>> factorWeight =
            factor -> 20;

    private static final Function<Integer, String> riskLevel =
            score -> score >= 70 ? "CRITICO"
                    : score >= 40 ? "ALTO"
                    : score >= 20 ? "MEDIO"
                    : "BAJO";

    private static final Function<SeverityLevel, String> severityDescription =
            level -> switch (level) {
                case CRITICO -> "Peligro inminente";
                case ALTO -> "Requiere atencion urgente";
                case MEDIO -> "Requiere seguimiento";
                case BAJO -> "Sin novedad";
            };

    public int calculateRisk(Incident incident) {
        return riskFactors.stream()
                .map(factor -> factor.test(incident) ? 1 : 0)
                .map(weight -> weight * 20)
                .reduce(0, Integer::sum);
    }

    public int calculateRiskWithHistory(Incident incident, long similarIncidentsCount) {
        int baseRisk = calculateRisk(incident);
        long reportFactor = Stream.of(similarIncidentsCount)
                .map(count -> count > 10 ? 20 : count > 5 ? 10 : count > 2 ? 5 : 0)
                .reduce(0, Integer::sum);
        return Math.min(100, baseRisk + (int) reportFactor);
    }

    public String getRiskLevel(int score) {
        return riskLevel.apply(score);
    }

    public SeverityLevel classifySeverity(int severityValue) {
        return SeverityLevel.fromValue(severityValue);
    }

    public String describeSeverity(SeverityLevel level) {
        return severityDescription.apply(level);
    }

    public List<SeverityLevel> getSeverityLevels() {
        return List.of(SeverityLevel.values());
    }
}

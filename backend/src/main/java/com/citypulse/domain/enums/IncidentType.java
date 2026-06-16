package com.citypulse.domain.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum IncidentType {
    ROBO(70),
    INCENDIO(90),
    ACCIDENTE(60),
    CORTE_ELECTRICO(40),
    CONGESTION(30);

    private final int baseSeverity;

    IncidentType(int baseSeverity) {
        this.baseSeverity = baseSeverity;
    }

    public int getBaseSeverity() {
        return baseSeverity;
    }

    public static Map<String, IncidentType> toMap() {
        return Arrays.stream(values())
                .collect(Collectors.toMap(
                        Enum::name,
                        java.util.function.Function.identity()
                ));
    }
}

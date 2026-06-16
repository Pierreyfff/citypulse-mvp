package com.citypulse.domain.enums;

public enum SeverityLevel {
    BAJO(25),
    MEDIO(50),
    ALTO(75),
    CRITICO(100);

    private final int maxValue;

    SeverityLevel(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public static SeverityLevel fromValue(int value) {
        if (value >= 75) return CRITICO;
        if (value >= 50) return ALTO;
        if (value >= 25) return MEDIO;
        return BAJO;
    }
}

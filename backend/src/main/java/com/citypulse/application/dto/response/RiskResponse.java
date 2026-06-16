package com.citypulse.application.dto.response;

public record RiskResponse(
        int riskScore,
        String level
) {

    public static RiskResponse fromScore(int score) {
        String level = score >= 70 ? "CRITICO"
                : score >= 40 ? "ALTO"
                : score >= 20 ? "MEDIO"
                : "BAJO";
        return new RiskResponse(score, level);
    }
}

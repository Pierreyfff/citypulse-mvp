package com.citypulse.application.service;

import com.citypulse.domain.enums.IncidentStatus;
import com.citypulse.domain.enums.IncidentType;
import com.citypulse.domain.model.Incident;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RiskEngine - Motor de calculo de riesgo")
class RiskEngineTest {

    private final RiskEngine riskEngine = new RiskEngine();

    @Nested
    @DisplayName("calculateRisk()")
    class CalculateRisk {

        @Test
        @DisplayName("retorna 0 cuando no se cumple ningun factor")
        void noFactors_returnsZero() {
            var incident = createIncident(IncidentType.CONGESTION, 10, false);
            assertEquals(0, riskEngine.calculateRisk(incident));
        }

        @Test
        @DisplayName("retorna 20 cuando severidad >= 70")
        void severity70Plus_returns20() {
            var incident = createIncident(IncidentType.CONGESTION, 70, false);
            assertEquals(20, riskEngine.calculateRisk(incident));
        }

        @Test
        @DisplayName("retorna 20 cuando tipo es INCENDIO")
        void incendio_returns20() {
            var incident = createIncident(IncidentType.INCENDIO, 10, false);
            assertEquals(20, riskEngine.calculateRisk(incident));
        }

        @Test
        @DisplayName("retorna 20 cuando tipo es ROBO")
        void robo_returns20() {
            var incident = createIncident(IncidentType.ROBO, 10, false);
            assertEquals(20, riskEngine.calculateRisk(incident));
        }

        @Test
        @DisplayName("retorna 20 cuando es zona sensible")
        void sensitiveZone_returns20() {
            var incident = createIncident(IncidentType.CONGESTION, 10, true);
            assertEquals(20, riskEngine.calculateRisk(incident));
        }

        @Test
        @DisplayName("retorna 40 cuando severidad >= 50 y zona sensible")
        void severity50AndSensitiveZone_returns40() {
            var incident = createIncident(IncidentType.CONGESTION, 50, true);
            assertEquals(40, riskEngine.calculateRisk(incident));
        }

        @Test
        @DisplayName("retorna 80 cuando se cumplen 4 de 5 factores (severity 80 + ROBO + zona sensible)")
        void allFactors_returns80() {
            var incident = createIncident(IncidentType.ROBO, 80, true);
            assertEquals(80, riskEngine.calculateRisk(incident));
        }
    }

    @Nested
    @DisplayName("getRiskLevel()")
    class GetRiskLevel {

        @Test
        @DisplayName("retorna BAJO para score < 20")
        void bajo() {
            assertEquals("BAJO", riskEngine.getRiskLevel(0));
            assertEquals("BAJO", riskEngine.getRiskLevel(19));
        }

        @Test
        @DisplayName("retorna MEDIO para score entre 20 y 39")
        void medio() {
            assertEquals("MEDIO", riskEngine.getRiskLevel(20));
            assertEquals("MEDIO", riskEngine.getRiskLevel(39));
        }

        @Test
        @DisplayName("retorna ALTO para score entre 40 y 69")
        void alto() {
            assertEquals("ALTO", riskEngine.getRiskLevel(40));
            assertEquals("ALTO", riskEngine.getRiskLevel(69));
        }

        @Test
        @DisplayName("retorna CRITICO para score >= 70")
        void critico() {
            assertEquals("CRITICO", riskEngine.getRiskLevel(70));
            assertEquals("CRITICO", riskEngine.getRiskLevel(100));
        }
    }

    @Nested
    @DisplayName("calculateRiskWithHistory() - Demostracion de Stream API (map/reduce)")
    class CalculateRiskWithHistory {

        @Test
        @DisplayName("incrementa riesgo segun cantidad de incidentes similares")
        void historyIncreasesRisk() {
            var incident = createIncident(IncidentType.CONGESTION, 10, false);
            int base = riskEngine.calculateRisk(incident);
            assertEquals(0, base);

            int with3Similar = riskEngine.calculateRiskWithHistory(incident, 3);
            assertEquals(5, with3Similar);

            int with10Similar = riskEngine.calculateRiskWithHistory(incident, 10);
            assertEquals(10, with10Similar);

            int with20Similar = riskEngine.calculateRiskWithHistory(incident, 20);
            assertEquals(20, with20Similar);
        }

        @Test
        @DisplayName("no supera 100 aunque suma supere el limite")
        void capsAt100() {
            var incident = createIncident(IncidentType.INCENDIO, 80, true);
            int risk = riskEngine.calculateRiskWithHistory(incident, 20);
            assertEquals(100, risk);
        }
    }

    @Nested
    @DisplayName("Demostracion de Programacion Funcional")
    class FunctionalProgrammingDemo {

        @Test
        @DisplayName("uso de Stream.map() y .reduce() en calculateRisk")
        void streamMapReduce() {
            var incident = createIncident(IncidentType.ROBO, 80, true);

            int risk = riskEngine.calculateRisk(incident);

            assertTrue(risk >= 0 && risk <= 100);
            assertEquals(80, risk);
        }

        @Test
        @DisplayName("uso de Predicate y funciones lambda en factores de riesgo")
        void predicateAndLambda() {
            var types = Stream.of(IncidentType.values())
                    .filter(t -> t.getBaseSeverity() >= 70)
                    .toList();

            assertTrue(types.contains(IncidentType.ROBO));
            assertTrue(types.contains(IncidentType.INCENDIO));
        }
    }

    private Incident createIncident(IncidentType type, int severity, boolean sensitiveZone) {
        return Incident.builder()
                .type(type)
                .status(IncidentStatus.REPORTADO)
                .description("Incidente de prueba")
                .address("Direccion de prueba")
                .latitude(-34.6037)
                .longitude(-58.3816)
                .severity(severity)
                .sensitiveZone(sensitiveZone)
                .userId(1L)
                .build();
    }
}

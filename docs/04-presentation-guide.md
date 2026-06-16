# Guia de Presentacion - CityPulse

## 1. Demo Flow (Tiempo estimado: 10-15 min)

### 1.1 Inicio: Arquitectura (2 min)
- Mostrar diagrama de Clean Architecture (4 capas)
- Explicar stack: Spring WebFlux + React + PostgreSQL + Docker

### 1.2 Docker Compose Up (1 min)
```bash
cd docker
docker compose up -d --build
docker compose ps
# Mostrar 3 contenedores: db, backend, frontend
```

### 1.3 Login y Dashboard (2 min)
- Abrir `http://localhost`
- Login con `admin / admin123`
- Mostrar KPIs, grafico de incidentes, gauge de riesgo

### 1.4 Crear Incidente (2 min)
- Navegar a Incidentes → Nuevo Incidente
- Tipo: INCENDIO, Severidad: 80, Zona sensible: Si
- Mostrar que el RiskScore se calcula automaticamente (80 = ALTO)

### 1.5 Mapa Interactivo (1 min)
- Navegar a Mapa
- Mostrar ubicacion del incidente en el mapa

### 1.6 WebSocket en Tiempo Real (2 min)
- Abrir dos pestañas del navegador
- En una, crear/modificar incidentes
- En la otra, mostrar que se actualiza sin refrescar

### 1.7 Estadisticas (1 min)
- Navegar a Estadisticas
- Mostrar graficos por tipo, estado, tendencias

---

## 2. Programacion Funcional - Puntos Clave

### RiskEngine.java (`backend/.../application/service/RiskEngine.java`)

**2.1 Stream.map() y .reduce()**
```java
public int calculateRisk(Incident incident) {
    return riskFactors.stream()
            .map(factor -> factor.test(incident) ? 1 : 0)
            .map(weight -> weight * 20)
            .reduce(0, Integer::sum);  // sum = 0, 20, 40, 60, 80
}
```

**2.2 Predicate como expresiones lambda**
```java
private static final List<Predicate<Incident>> riskFactors = List.of(
    incident -> incident.getSeverity() >= 70,          // Factor severidad
    incident -> incident.getType() == IncidentType.INCENDIO,  // Factor incendio
    incident -> incident.getType() == IncidentType.ROBO,       // Factor robo
    incident -> incident.isSensitiveZone(),                     // Factor zona sensible
    incident -> incident.getSeverity() >= 50 && incident.isSensitiveZone()  // Combinado
);
```

**2.3 Optional y funciones puras**
```java
// En controllers: Optional para filtros opcionales
return Optional.ofNullable(status)
        .map(incidentService::findByStatus)
        .orElseGet(() -> Optional.ofNullable(type)
                .map(incidentService::findByType)
                .orElseGet(incidentService::findAll))
        .collectList();

// RiskEngine: funcion pura (sin efectos secundarios)
public String getRiskLevel(int score) {
    return score >= 70 ? "CRITICO"
         : score >= 40 ? "ALTO"
         : score >= 20 ? "MEDIO"
         : "BAJO";
}
```

**2.4 groupingBy en StatsService**
```java
// (La implementacion usa groupingBy para estadisticas)
```

---

## 3. Programacion Reactiva - Puntos Clave

### 3.1 Mono/Flux en servicios
```java
// Composicion reactiva con flatMap
public Mono<IncidentResponse> create(IncidentRequest request, Long userId) {
    return Mono.just(request)
            .map(req -> incidentMapper.toDomain(req, userId))
            .map(incident -> { /* ... */ })
            .map(entityMapper::toEntity)
            .flatMap(incidentRepository::save)  // I/O no-blocking
            .map(entityMapper::toDomain)
            .flatMap(incident -> eventBus.emitCreated(incident)
                    .thenReturn(incident))
            .map(incidentMapper::toResponse);
}
```

### 3.2 Manejo de errores reactivo
```java
public Mono<IncidentResponse> findById(Long id) {
    return incidentRepository.findById(id)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Incidente", id)))
            .map(incident -> /* ... */);
}
```

### 3.3 Sinks.Many para WebSocket
```java
// EventBus con backpressure
private final Sinks.Many<IncidentEvent> sink =
        Sinks.many().multicast().onBackpressureBuffer();

public Flux<IncidentEvent> getEventStream() {
    return sink.asFlux();
}

public Mono<Incident> emitCreated(Incident incident) {
    return Mono.fromRunnable(() -> {
        var event = new IncidentEvent("INCIDENTE_CREADO", incident);
        Sinks.EmitResult result = sink.tryEmitNext(event);
        if (result.isFailure()) {
            sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
        }
    }).thenReturn(incident);
}
```

### 3.4 WebSocket Handler
```java
// WebSocketConfig.java
// Configuracion reactiva del handler WebSocket
```

---

## 4. Seguridad JWT - Flujo

```
Login → /api/auth/login → JwtProvider.generateToken() → Bearer JWT
Cada request → JwtAuthenticationFilter.filter() → 
  Extrae token del header → Valida firma → 
  ReactiveSecurityContextHolder.withAuthentication()
```

---

## 5. Arquitectura Limpia - Mapeo de Capas

| Capa | Paquete | Contenido |
|------|---------|-----------|
| Domain | `domain/` | Models, Enums, Repository interfaces, Exceptions |
| Application | `application/` | Services, DTOs, Mappers |
| Infrastructure | `infrastructure/` | JPA Entities, R2DBC Repos, Security, WebSocket |
| Presentation | `presentation/` | Controllers, Config, ExceptionHandler |

Flujo de datos: `Controller → Service → Repository (interface) → R2DBC Repository → DB`

---

## 6. Endpoints Clave para Probar

```bash
# Autenticacion
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Incidentes
curl http://localhost:8080/api/incidents \
  -H "Authorization: Bearer <token>"

# Dashboard
curl http://localhost:8080/api/dashboard \
  -H "Authorization: Bearer <token>"

# Swagger UI
# http://localhost:8080/swagger-ui.html
```

---

## 7. Testing

```bash
cd backend
mvn test
# 25 tests: RiskEngine (15) + IncidentService (10)
```

### Tests incluidos:
- `RiskEngineTest`: Calculo de riesgo con Stream API, niveles, factores
- `IncidentServiceTest`: CRUD reactivo con Mockito + StepVerifier

---

## 8. Checklist de Verificacion

- [ ] `docker compose up -d --build` compila sin errores
- [ ] Login funciona con los 3 usuarios seed
- [ ] CRUD de incidentes funciona
- [ ] Dashboard muestra KPIs correctos
- [ ] Filtros por status/type funcionan
- [ ] WebSocket actualiza en tiempo real
- [ ] Mapa muestra incidentes
- [ ] Estadisticas se generan correctamente
- [ ] Swagger UI carga (http://localhost:8080/swagger-ui.html)
- [ ] Tests pasan: `mvn test` (25/25)
- [ ] Acceso sin token retorna 401
- [ ] Frontend SPA funciona (todas las rutas)

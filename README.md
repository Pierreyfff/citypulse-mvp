# CityPulse - Sistema Inteligente de Gestion y Monitoreo de Emergencias Urbanas

> Aplicacion full-stack reactiva para el monitoreo y gestion de emergencias urbanas en tiempo real, construida con Spring WebFlux y React.

---

## Stack Tecnologico

### Backend
| Tecnologia | Version | Proposito |
|------------|---------|-----------|
| Java | 21 | Lenguaje principal |
| Spring Boot | 3.3.0 | Framework base |
| Spring WebFlux | 3.3.0 | API reactiva no-blocking |
| Spring Security | 3.3.0 | Autenticacion JWT |
| Spring Data R2DBC | 3.3.0 | Persistencia reactiva |
| PostgreSQL 16 | 16 | Base de datos |
| R2DBC PostgreSQL | 1.0.5 | Driver reactivo PostgreSQL |
| JJWT | 0.12.6 | Tokens JWT |
| MapStruct | 1.5.5 | Mapeo de objetos |
| Lombok | ultimo | Reduccion de boilerplate |
| SpringDoc OpenAPI | 2.5.0 | Documentacion Swagger |

### Frontend
| Tecnologia | Version | Proposito |
|------------|---------|-----------|
| React | 18.3.1 | UI |
| TypeScript | 5.x | Tipado estatico |
| Material UI | 5.16.0 | Componentes visuales |
| React Query | 5.51.0 | Estado del servidor |
| React Router | 6.26.0 | Routing SPA |
| Axios | 1.7.2 | HTTP client |
| Recharts | 2.12.7 | Graficos |
| Leaflet / react-leaflet | 1.9.4 / 4.2.1 | Mapas interactivos |
| SockJS / STOMP | 1.6.1 / 2.3.3 | WebSocket |

### Infraestructura
| Tecnologia | Proposito |
|------------|-----------|
| Docker | Contenedores |
| Docker Compose | Orquestacion (3 servicios) |
| Nginx | Servidor web / proxy reverso |

---

## Arquitectura

### Clean Architecture (4 capas)

```
┌─────────────────────────────────────────────┐
│             Presentation                    │
│  Controllers, Config, GlobalExceptionHandler│
├─────────────────────────────────────────────┤
│             Application                     │
│  Services, DTOs, Mappers, Ports            │
├─────────────────────────────────────────────┤
│              Domain                         │
│  Models, Enums, Repository Interfaces,      │
│  Exceptions                                │
├─────────────────────────────────────────────┤
│           Infrastructure                    │
│  JPA/R2DBC Entities, Repositories,          │
│  Security (JWT), WebSocket EventBus         │
└─────────────────────────────────────────────┘
```

### Flujo de datos

```
Cliente Web → Nginx (puerto 80) → Backend Netty (8080) → R2DBC → PostgreSQL
                                    ↕
                              WebSocket (Sinks.Many)
                                    ↕
                              IncidentEventBus → Clientes conectados
```

---

## Requisitos

- Docker Desktop 4.x+ (recomendado)
- O alternativamente: Java 21+, Node.js 20+, PostgreSQL 16+, Maven 3.9+

---

## Inicio Rapido (Docker Compose)

```bash
# Clonar el repositorio
cd citypulse

# Construir e iniciar todos los servicios
cd docker
docker compose up -d --build

# Esperar a que los contenedores esten saludables
docker compose ps

# Acceder a la aplicacion
# Frontend: http://localhost
# Backend API: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```

### Usuarios de prueba

| Usuario | Contrasena | Rol |
|---------|-----------|-----|
| `admin` | `admin123` | ADMIN |
| `operador1` | `operador123` | OPERADOR |
| `ciudadano1` | `ciudadano123` | CIUDADANO |

---

## Inicio Manual

### Backend

```bash
# Requiere: Java 21+, PostgreSQL 16+, Maven 3.9+

# Crear base de datos PostgreSQL
createdb citypulse

# Configurar variables de entorno (o usar defaults en application.yml)
export SPRING_R2DBC_URL=r2dbc:postgresql://localhost:5432/citypulse
export SPRING_R2DBC_USERNAME=citypulse
export SPRING_R2DBC_PASSWORD=citypulse123

# Compilar y ejecutar
cd backend
mvn clean package -DskipTests
java -jar target/citypulse-backend-1.0.0.jar
```

### Frontend

```bash
# Requiere: Node.js 20+

cd frontend
npm install
npm run dev
# Acceder en http://localhost:5173
```

---

## API REST

### Autenticacion (`/api/auth`)
| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| POST | `/api/auth/register` | Registrar nuevo usuario |
| POST | `/api/auth/login` | Iniciar sesion (obtener JWT) |
| POST | `/api/auth/refresh` | Refrescar token |

### Incidentes (`/api/incidents`)
| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| GET | `/api/incidents` | Listar incidentes (filtros: `?status=`, `?type=`) |
| GET | `/api/incidents/{id}` | Obtener incidente por ID |
| POST | `/api/incidents` | Crear incidente |
| PUT | `/api/incidents/{id}` | Actualizar incidente |
| PATCH | `/api/incidents/{id}/status?status=` | Cambiar estado |
| DELETE | `/api/incidents/{id}` | Eliminar incidente |

### Dashboard (`/api/dashboard`)
| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| GET | `/api/dashboard` | KPIs: total, activos, resueltos, criticos |

### Estadisticas (`/api/stats`)
| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| GET | `/api/stats` | Stats agrupadas por tipo, estado, dia, mes |

### Usuarios (`/api/users`)
| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| GET | `/api/users` | Listar usuarios (solo ADMIN) |
| POST | `/api/users` | Crear usuario |

### WebSocket
| Endpoint | Protocolo | Descripcion |
|----------|-----------|-------------|
| `/ws/incidents` | SockJS / STOMP | Eventos en tiempo real |

---

## Frontend - Rutas

| Ruta | Pagina | Acceso |
|------|--------|--------|
| `/login` | Inicio de sesion | Publico |
| `/register` | Registro | Publico |
| `/dashboard` | Dashboard con KPIs, graficos, gauge de riesgo | Autenticado |
| `/incidents` | Gestion de incidentes (CRUD) | Autenticado |
| `/map` | Mapa interactivo de incidentes | ADMIN, OPERADOR, CIUDADANO |
| `/stats` | Estadisticas detalladas | ADMIN, OPERADOR |
| `/users` | Administracion de usuarios | ADMIN |

---

## Programacion Funcional - Demostraciones

### Stream API en RiskEngine (`backend/.../RiskEngine.java`)

```java
// map/filter/reduce para calcular riesgo
public int calculateRisk(Incident incident) {
    return riskFactors.stream()           // Stream<Predicate<Incident>>
            .map(factor -> factor.test(incident) ? 1 : 0)  // map: Predicate → int
            .map(weight -> weight * 20)                     // map: transformacion
            .reduce(0, Integer::sum);                       // reduce: acumulacion
}

// Uso de Optional y lambda
public int calculateRiskWithHistory(Incident incident, long similarCount) {
    long reportFactor = Stream.of(similarCount)
            .map(count -> count > 10 ? 20 : count > 5 ? 10 : count > 2 ? 5 : 0)
            .reduce(0, Integer::sum);
    return Math.min(100, calculateRisk(incident) + (int) reportFactor);
}
```

### Predicate y groupingBy

```java
// 5 factores de riesgo como Predicate<Incident>
private static final List<Predicate<Incident>> riskFactors = List.of(
    incident -> incident.getSeverity() >= 70,
    incident -> incident.getType() == IncidentType.INCENDIO,
    incident -> incident.getType() == IncidentType.ROBO,
    incident -> incident.isSensitiveZone(),
    incident -> incident.getSeverity() >= 50 && incident.isSensitiveZone()
);
```

---

## Programacion Reactiva - Demostraciones

### Mono/Flux en controladores y servicios (`backend/.../IncidentService.java`)

```java
// Composicion reactiva con flatMap
public Mono<IncidentResponse> create(IncidentRequest request, Long userId) {
    return Mono.just(request)
            .map(req -> incidentMapper.toDomain(req, userId))
            .map(incident -> { /* calcular riesgo */ ... })
            .map(entityMapper::toEntity)
            .flatMap(incidentRepository::save)          // I/O no-blocking
            .map(entityMapper::toDomain)
            .flatMap(incident -> eventBus.emitCreated(incident)  // evento async
                    .thenReturn(incident))
            .map(incidentMapper::toResponse);
}
```

### Sinks.Many para WebSocket (`backend/.../IncidentEventBus.java`)

```java
// Event bus reactivo con backpressure
private final Sinks.Many<IncidentEvent> sink =
        Sinks.many().multicast().onBackpressureBuffer();

public Flux<IncidentEvent> getEventStream() {
    return sink.asFlux();  // Cold → Hot publisher conversion
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

### Manejo de errores reactivo

```java
public Mono<IncidentResponse> findById(Long id) {
    return incidentRepository.findById(id)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Incidente", id)))
            .map(entityMapper::toDomain)
            .map(incidentMapper::toResponse);
}
```

---

## Estructura del Proyecto

```
citypulse/
├── backend/
│   ├── pom.xml
│   └── src/main/java/com/citypulse/
│       ├── CityPulseApplication.java
│       ├── application/
│       │   ├── dto/{request,response}/
│       │   ├── mapper/
│       │   └── service/
│       ├── domain/
│       │   ├── enums/
│       │   ├── exception/
│       │   ├── model/
│       │   └── repository/
│       ├── infrastructure/
│       │   ├── persistence/{entity,mapper,repository}/
│       │   ├── security/
│       │   └── websocket/
│       └── presentation/
│           ├── config/
│           └── controller/
├── frontend/
│   ├── package.json
│   └── src/
│       ├── api/
│       ├── components/{common,dashboard,incidents,layout,map,stats}/
│       ├── context/
│       ├── hooks/
│       ├── pages/
│       ├── routes/
│       ├── types/
│       └── utils/
├── docker/
│   ├── docker-compose.yml
│   ├── backend.Dockerfile
│   ├── frontend.Dockerfile
│   ├── nginx.conf
│   └── .env.example
└── docs/
    ├── 01-analysis.md
    ├── 02-architecture.md
    └── 03-database.md
```

---

## Variables de Entorno

| Variable | Default | Descripcion |
|----------|---------|-------------|
| `SPRING_R2DBC_URL` | `r2dbc:postgresql://localhost:5432/citypulse` | URL de base de datos |
| `SPRING_R2DBC_USERNAME` | `citypulse` | Usuario BD |
| `SPRING_R2DBC_PASSWORD` | `citypulse123` | Contrasena BD |
| `JWT_SECRET` | (valor por defecto) | Clave secreta JWT (min 256 bits) |
| `JWT_EXPIRATION` | `3600000` | TTL token acceso (1h) |
| `JWT_REFRESH_EXPIRATION` | `86400000` | TTL refresh token (24h) |

---

## Testing

```bash
# Backend: Tests unitarios con JUnit 5 + Mockito + StepVerifier
cd backend
mvn test

# Tests incluidos:
# - RiskEngineTest: 15 tests (calculo de riesgo, niveles, Stream API)
# - IncidentServiceTest: 10 tests (CRUD reactivo con mocks, eventos)
```

---

## Troubleshooting

### `ERR_INCOMPLETE_CHUNKED_ENCODING`
- **Causa:** Nginx recibe chunked encoding de WebFlux en respuestas vacias
- **Solucion:** Los controladores retornan `Mono<List<T>>` en lugar de `Flux<T>` para endpoints de colecciones
- **Config Nginx:** `proxy_buffering off; chunked_transfer_encoding on;`

### Error de conexion a BD
- Verificar que PostgreSQL este corriendo
- Verificar credenciales en variables de entorno o `application.yml`

### WebSocket no conecta
- Verificar que el proxy de Nginx tenga `proxy_set_header Upgrade $http_upgrade;`
- Verificar timeout: `proxy_read_timeout 86400s;`

---

## Licencia

Proyecto universitario - Todos los derechos reservados.

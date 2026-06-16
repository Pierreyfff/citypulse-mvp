# CityPulse - Progreso del Proyecto

## Fases Completadas
- **Fase 1**: Análisis y requerimientos (`docs/01-analysis.md`)
- **Fase 2**: Arquitectura (`docs/02-architecture.md`)
- **Fase 3**: Diseño de Base de Datos (`docs/03-database.md`)
- **Fase 4**: Entidades R2DBC y Repositorios
- **Fase 5**: DTOs, Mappers (MapStruct), Value Objects
- **Fase 6**: Servicios (IncidentService, RiskEngine, StatsService, etc.)
- **Fase 7**: Event Bus (WebSocket con Sinks.Many + backpressure)
- **Fase 8**: Seguridad JWT + Spring WebFlux Security
- **Fase 9**: Controladores REST (5 endpoints)
- **Fase 10**: Global Exception Handler + OpenAPI + CORS
- **Fase 11**: Data Initializer (seed users + datos demo)
- **Fase 12**: Frontend (React + TS + MUI + React Query + Router + Leaflet + Recharts)
- **Fase 13**: Login/Register, Dashboard, Incidentes CRUD, Mapa, Stats
- **Fase 14**: WebSocket en frontend (SockJS + STOMP)
- **Fase 15**: Docker Compose (PostgreSQL 16, Netty backend, Nginx frontend)
- **Fase 16**: Pruebas (25 tests: RiskEngine 15, IncidentService 10)
- **Fase 17**: Documentación (README, Presentation Guide)
- **Fase 18**: Feature - Roles (CIUDADANO solo ve sus incidentes, no edita estado ni elimina)
- **Fase 19**: Feature - Severidad por niveles con SeverityLevel enum, auto-ubicación en formulario, iconos por tipo, riesgo basado en niveles

## Pendientes para próxima sesión

### High Priority
1. **Hardcodeo de dependencias en pom.xml**: Agregar las propiedades y dependencias exactas de MapStruct, OpenAPI, etc. al `pom.xml` para que el build no dependa de resolución de versiones.
2. **DELETE /api/incidents/{id}**: Bug de Netty - después de un POST con body inválido, la conexión se corrompe y DELETE falla con connection reset. Investigar si es por `@RequestBody` record validation + WebFlux pipeline.
3. **WebSocket reconnect**: Si el backend se cae, el frontend no reconecta automáticamente. Agregar lógica de reconexión con backoff en `WebSocketContext.tsx`.

### Medium Priority
4. **Lazy loading en mapa**: Los markers en `IncidentMap.tsx` se renderizan todos de una. Para muchas incidencias (>500), usar clustering con `react-leaflet-cluster`.
5. **Paginación en incidentes**: `GET /api/incidents` devuelve todos. Agregar `?page=&size=` con `Pageable` de Spring.
6. **Internacionalización (i18n)**: Los textos están en español duro. Migrar a react-i18next o similar.

### Low Priority / Nice to Have
7. **Rate limiting**: Agregar bucket4j o similar para evitar abuso de endpoints públicos (`/api/auth/**`).
8. **Modo oscuro**: Alternar tema MUI entre light/dark persistido en localStorage.
9. **Notificaciones push**: Web Push API para alertar operadores de incidentes CRITICO en tiempo real.
10. **Tests de integración**: Agregar `@SpringBootTest` con Testcontainers para probar el flujo completo R2DBC + Security.

## Cómo iniciar el proyecto
```powershell
cd C:\Users\Maria\Projects\CityPulse
docker compose -f docker/docker-compose.yml down --rmi all -v
docker compose -f docker/docker-compose.yml build --no-cache
docker compose -f docker/docker-compose.yml up -d
```

## Credenciales
| Rol | Usuario | Contraseña |
|---|---|---|
| ADMIN | admin | admin123 |
| OPERADOR | operador1 | operador123 |
| CIUDADANO | ciudadano1 | ciudadano123 |

## Swagger (OpenAPI)
- URL: http://localhost:8080/swagger-ui.html
- Autenticación: Primero hacer login en `POST /api/auth/login` con admin/admin123, copiar el token JWT, y pegarlo en el botón "Authorize" del Swagger (formato: `Bearer <token>`)

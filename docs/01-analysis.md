# FASE 1: ANÁLISIS COMPLETO - CityPulse

## 1. VISIÓN GENERAL

**CityPulse** es un Sistema Inteligente de Gestión y Monitoreo de Emergencias Urbanas en Tiempo Real. Permite a ciudadanos reportar incidentes, a operadores gestionarlos, y a administradores visualizar estadísticas y dashboards en vivo.

## 2. OBJETIVOS DEL SISTEMA

- Reportar incidentes urbanos en tiempo real
- Clasificar y priorizar emergencias automáticamente
- Visualizar mapa interactivo con incidentes geolocalizados
- Proveer dashboard con KPIs en vivo
- Calcular nivel de riesgo usando programación funcional
- Notificar cambios mediante WebSocket
- Soportar 3 roles: ADMIN, OPERADOR, CIUDADANO

## 3. ANÁLISIS DE ARQUITECTURA

### 3.1 Clean Architecture (4 capas)

```
┌─────────────────────────────────────────┐
│           PRESENTATION                   │
│     (REST Controllers, WebSocket)        │
├─────────────────────────────────────────┤
│           APPLICATION                    │
│       (Use Cases, Services, DTOs)        │
├─────────────────────────────────────────┤
│           DOMAIN                         │
│    (Entities, Value Objects, Enums)      │
├─────────────────────────────────────────┤
│          INFRASTRUCTURE                  │
│   (R2DBC Repositories, Security, JWT)    │
└─────────────────────────────────────────┘
```

### 3.2 Principios SOLID aplicados

| Principio | Aplicación |
|-----------|-----------|
| SRP | Cada clase tiene una única responsabilidad |
| OCP | Abierto a extensión, cerrado a modificación |
| LSP | Subtipos sustituibles |
| ISP | Interfaces específicas por cliente |
| DIP | Dependencias invertidas vía interfaces |

## 4. ANÁLISIS DE MÓDULOS

### 4.1 Autenticación
- Registro de ciudadanos (público)
- Login con JWT
- Refresh Token
- Roles: ADMIN, OPERADOR, CIUDADANO
- Endpoints protegidos por rol

### 4.2 Usuarios
- CRUD completo (solo ADMIN)
- Perfiles por rol
- Estado activo/inactivo

### 4.3 Incidentes
- Tipos: ROBO, INCENDIO, ACCIDENTE, CORTE_ELECTRICO, CONGESTION
- Estados: REPORTADO, EN_PROCESO, RESUELTO
- Geolocalización (latitud, longitud)
- CRUD con validaciones

### 4.4 Motor de Riesgo
- Entrada: gravedad, cantidad reportes, zona sensible
- Procesamiento: map, filter, reduce
- Salida: score 0-100
- Ejecución reactiva con Mono/Flux

### 4.5 Event Bus
- Sinks.Many para emisión
- Suscripción reactiva
- Eventos: incidente creado, actualizado, resuelto

### 4.6 WebSocket
- Conexión vía SockJS + STOMP
- Topics: /topic/incidentes
- Notificaciones push en tiempo real

### 4.7 Dashboard
- KPIs: total, activos, resueltos, críticos
- Actualización en tiempo real
- Gráficos con Recharts

### 4.8 Mapa
- Leaflet con marcadores dinámicos
- Colores: verde (bajo), amarillo (medio), rojo (alto)
- Filtros por tipo, estado, prioridad

### 4.9 Estadísticas
- Agrupación por tipo, estado, día, mes
- Uso de groupingBy, map, reduce
- Visualización con Recharts

## 5. ANÁLISIS DE BASE DE DATOS

### 5.1 Entidades principales

```
users (id, username, email, password, role, active, created_at, updated_at)
incidents (id, tipo, estado, descripcion, direccion, latitud, longitud, 
           gravedad, zona_sensible, riesgo_score, user_id, created_at, updated_at)
refresh_tokens (id, token, user_id, expires_at, created_at)
```

### 5.2 Relaciones
- User 1:N Incidents (un usuario reporta muchos incidentes)
- User 1:N RefreshTokens

## 6. ANÁLISIS DE PARADIGMAS

### 6.1 Programación Funcional (obligatorio)

| Concepto | Ubicación |
|----------|-----------|
| Lambda Expressions | Services, Mappers |
| Method References | Streams, Validators |
| Optional | Return types, Null safety |
| Predicate | Filtros dinámicos |
| Function | Transformaciones |
| Consumer | Procesamiento batch |
| Supplier | Fábricas, lazy eval |
| Streams API | Colecciones, reportes |
| map/filter/reduce | Motor de riesgo, estadísticas |
| flatMap | Joins reactivos |
| groupingBy | Estadísticas agrupadas |
| collect | Terminal operations |
| Pure functions | Risk Engine |
| Immutability | DTOs, Records |
| Function composition | Pipelines de procesamiento |

### 6.2 Programación Reactiva (obligatorio)

| Concepto | Ubicación |
|----------|-----------|
| Mono | CRUD individual, Auth |
| Flux | Listas, eventos batch |
| Reactive Streams | Capa de datos |
| Backpressure | Estrategia de suscripción |
| Event Driven | Event Bus |
| Publisher/Suscriber | WebSocket |
| Sinks.Many | Event Bus interno |
| Non Blocking I/O | Toda la capa WebFlux |

## 7. STACK TECNOLÓGICO

### Backend
- Java 21 + Spring Boot 3 + WebFlux
- Spring Security + JWT
- R2DBC + PostgreSQL
- Lombok + MapStruct + Validation
- WebSocket + OpenAPI

### Frontend
- React 18 + TypeScript
- Material UI + React Router
- React Query + Axios
- Recharts + Leaflet
- SockJS + STOMP

### Infraestructura
- Docker + Docker Compose
- PostgreSQL 16

## 8. RIESGOS Y MITIGACIONES

| Riesgo | Mitigación |
|--------|-----------|
| Complejidad reactiva | Separar lógica reactiva de funcional |
| Curva de aprendizaje R2DBC | Modelo simple, consultas básicas |
| Tiempo limitado | Priorizar MVP funcional |
| WebSocket complejo | Usar STOMP que abstrae complejidad |

## 9. PLAN DE FASES ESTIMADO

| Fase | Descripción | Esfuerzo |
|------|-------------|----------|
| 1 | Análisis | Completado |
| 2 | Arquitectura | Siguiente |
| 3-4 | Dominio y BD | 1 sesión |
| 5-7 | Backend base | 2 sesiones |
| 8-9 | Funcional y Reactivo | 1 sesión |
| 10-11 | Event Bus y WS | 1 sesión |
| 12-15 | Frontend | 3 sesiones |
| 16 | Docker | 1 sesión |
| 17 | Testing | 1 sesión |
| 18-19 | Documentación e integración | 1 sesión |
| 20 | Sustentación | 1 sesión |

---

**FASE 1 COMPLETADA.**

¿Apruebas el análisis para continuar con la **FASE 2: ARQUITECTURA**?

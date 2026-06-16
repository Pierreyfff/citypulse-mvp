# FASE 2: ARQUITECTURA - CityPulse

## 1. ARQUITECTURA GENERAL

```
┌─────────────────────────────────────────────────────────┐
│                     CLIENTE (React + TS)                  │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌───────────┐  │
│  │   Auth   │ │Incidents │ │Dashboard │ │   Map     │  │
│  │   Pages  │ │  Pages   │ │  Pages   │ │   View    │  │
│  └────┬─────┘ └────┬─────┘ └────┬─────┘ └─────┬─────┘  │
│       │            │            │              │        │
│  ┌────┴────────────┴────────────┴──────────────┴────┐   │
│  │              React Query + Axios                  │   │
│  └───────────────────────┬──────────────────────────┘   │
│  ┌───────────────────────┴──────────────────────────┐   │
│  │          SockJS + STOMP (WebSocket)               │   │
│  └──────────────────────────────────────────────────┘   │
└──────────────────────────┬──────────────────────────────┘
                           │ HTTP / WS
┌──────────────────────────┴──────────────────────────────┐
│              SPRING WEBFLUX (Netty)                      │
│  ┌──────────────────────────────────────────────────┐   │
│  │               PRESENTATION LAYER                  │   │
│  │  REST Controllers  /  WebSocket Handlers         │   │
│  └──────────────────────┬───────────────────────────┘   │
│  ┌──────────────────────┴───────────────────────────┐   │
│  │              APPLICATION LAYER                    │   │
│  │  Services / Use Cases / DTOs / Mappers           │   │
│  │  Risk Engine / Event Bus                         │   │
│  └──────────────────────┬───────────────────────────┘   │
│  ┌──────────────────────┴───────────────────────────┐   │
│  │               DOMAIN LAYER                       │   │
│  │  Entities / Value Objects / Enums / Interfaces  │   │
│  └──────────────────────┬───────────────────────────┘   │
│  ┌──────────────────────┴───────────────────────────┐   │
│  │            INFRASTRUCTURE LAYER                   │   │
│  │  R2DBC Repos / JWT / Security / WebSocket        │   │
│  └──────────────────────────────────────────────────┘   │
└──────────────────────────┬──────────────────────────────┘
                           │ R2DBC
┌──────────────────────────┴──────────────────────────────┐
│                    POSTGRESQL                            │
└─────────────────────────────────────────────────────────┘
```

## 2. ESTRUCTURA DE PAQUETES - BACKEND

```
backend/
├── pom.xml
└── src/main/java/com/citypulse/
    ├── CityPulseApplication.java
    │
    ├── domain/
    │   ├── model/
    │   │   ├── User.java
    │   │   ├── Incident.java
    │   │   └── RefreshToken.java
    │   ├── enums/
    │   │   ├── Role.java
    │   │   ├── IncidentType.java
    │   │   └── IncidentStatus.java
    │   ├── exception/
    │   │   ├── DomainException.java
    │   │   ├── ResourceNotFoundException.java
    │   │   └── UnauthorizedException.java
    │   └── repository/
    │       ├── UserRepository.java        (interface)
    │       └── IncidentRepository.java    (interface)
    │
    ├── application/
    │   ├── dto/
    │   │   ├── request/
    │   │   │   ├── LoginRequest.java
    │   │   │   ├── RegisterRequest.java
    │   │   │   ├── RefreshTokenRequest.java
    │   │   │   ├── UserRequest.java
    │   │   │   └── IncidentRequest.java
    │   │   └── response/
    │   │       ├── AuthResponse.java
    │   │       ├── UserResponse.java
    │   │       ├── IncidentResponse.java
    │   │       ├── DashboardResponse.java
    │   │       ├── RiskResponse.java
    │   │       └── StatsResponse.java
    │   ├── service/
    │   │   ├── AuthService.java
    │   │   ├── UserService.java
    │   │   ├── IncidentService.java
    │   │   ├── RiskEngine.java
    │   │   ├── DashboardService.java
    │   │   └── StatsService.java
    │   ├── mapper/
    │   │   ├── UserMapper.java
    │   │   ├── IncidentMapper.java
    │   │   └── DashboardMapper.java
    │   └── port/
    │       ├── AuthPort.java
    │       ├── UserPort.java
    │       └── IncidentPort.java
    │
    ├── infrastructure/
    │   ├── persistence/
    │   │   ├── entity/
    │   │   │   ├── UserEntity.java
    │   │   │   ├── IncidentEntity.java
    │   │   │   └── RefreshTokenEntity.java
    │   │   ├── repository/
    │   │   │   ├── ReactiveUserRepository.java
    │   │   │   ├── ReactiveIncidentRepository.java
    │   │   │   └── ReactiveRefreshTokenRepository.java
    │   │   └── mapper/
    │   │       ├── UserEntityMapper.java
    │   │       └── IncidentEntityMapper.java
    │   ├── security/
    │   │   ├── JwtProvider.java
    │   │   ├── SecurityConfig.java
    │   │   ├── ReactiveUserDetailsService.java
    │   │   └── JwtAuthenticationFilter.java
    │   └── websocket/
    │       ├── WebSocketConfig.java
    │       └── IncidentEventBus.java
    │
    └── presentation/
        ├── controller/
        │   ├── AuthController.java
        │   ├── UserController.java
        │   ├── IncidentController.java
        │   ├── DashboardController.java
        │   └── StatsController.java
        └── config/
            ├── OpenApiConfig.java
            └── CorsConfig.java
```

## 3. ESTRUCTURA DE CARPETAS - FRONTEND

```
frontend/
├── package.json
├── tsconfig.json
├── index.html
├── vite.config.ts
├── public/
│   └── favicon.ico
└── src/
    ├── main.tsx
    ├── App.tsx
    │
    ├── api/
    │   ├── axios.ts
    │   ├── auth.ts
    │   ├── incidents.ts
    │   ├── dashboard.ts
    │   └── stats.ts
    │
    ├── types/
    │   ├── auth.ts
    │   ├── incident.ts
    │   ├── dashboard.ts
    │   └── common.ts
    │
    ├── context/
    │   ├── AuthContext.tsx
    │   └── WebSocketContext.tsx
    │
    ├── hooks/
    │   ├── useAuth.ts
    │   ├── useIncidents.ts
    │   ├── useDashboard.ts
    │   └── useWebSocket.ts
    │
    ├── components/
    │   ├── layout/
    │   │   ├── AppLayout.tsx
    │   │   ├── Navbar.tsx
    │   │   └── Sidebar.tsx
    │   ├── common/
    │   │   ├── LoadingSpinner.tsx
    │   │   ├── ErrorAlert.tsx
    │   │   ├── StatusChip.tsx
    │   │   └── ProtectedRoute.tsx
    │   ├── incidents/
    │   │   ├── IncidentList.tsx
    │   │   ├── IncidentForm.tsx
    │   │   ├── IncidentCard.tsx
    │   │   └── IncidentFilters.tsx
    │   ├── dashboard/
    │   │   ├── KpiCard.tsx
    │   │   ├── IncidentChart.tsx
    │   │   ├── RiskGauge.tsx
    │   │   └── RecentIncidents.tsx
    │   ├── map/
    │   │   ├── IncidentMap.tsx
    │   │   ├── MapMarker.tsx
    │   │   └── MapFilters.tsx
    │   └── stats/
    │       ├── StatsChart.tsx
    │       └── StatsTable.tsx
    │
    ├── pages/
    │   ├── LoginPage.tsx
    │   ├── RegisterPage.tsx
    │   ├── DashboardPage.tsx
    │   ├── IncidentsPage.tsx
    │   ├── IncidentDetailPage.tsx
    │   ├── MapPage.tsx
    │   ├── StatsPage.tsx
    │   └── UsersPage.tsx
    │
    ├── routes/
    │   └── AppRoutes.tsx
    │
    └── utils/
        ├── formatDate.ts
        └── riskColor.ts
```

## 4. FLUJO DE DATOS - AUTENTICACIÓN

```
Register → POST /api/auth/register → AuthService → UserRepository → DB
             ↓
          AuthResponse (JWT + RefreshToken)

Login → POST /api/auth/login → AuthService → JwtProvider → DB
          ↓
       AuthResponse (JWT + RefreshToken)

Request → JwtAuthenticationFilter → JwtProvider.validate() → SecurityContext
           ↓
        Permite/Deniega acceso
```

## 5. FLUJO DE DATOS - INCIDENTES

```
POST /api/incidents → IncidentController → IncidentService
                      ↓
              RiskEngine.calculateRisk(incident)
                      ↓
              map(gravedad, cantidad, zona) → filter → reduce → score
                      ↓
              IncidentEventBus.emit(incident)
                      ↓
              WebSocket → /topic/incidentes → Clientes
                      ↓
              ReactiveIncidentRepository.save() → DB
```

## 6. FLUJO DE DATOS - DASHBOARD EN TIEMPO REAL

```
IncidentService.create()
    ↓
IncidentEventBus (Sinks.Many)
    ↓
WebSocket Handler
    ↓
STOMP /topic/incidentes
    ↓
React (SockJS + STOMP Client)
    ↓
React Query Cache Invalidation
    ↓
Dashboard re-render
```

## 7. DIAGRAMA DE COMPONENTES REACTIVOS

```
                  Flux/Mono
                     │
        ┌────────────┼────────────┐
        │            │            │
   Controller    Service    Repository
        │            │            │
   Mono<T>     Flux<T>    Flux<T>
        │            │            │
   map/flatMap  filter/map   flatMap
        │            │            │
   WebSocket ← EventBus ← Sinks.Many
```

## 8. INTERFACES DE DOMINIO (CONTRATOS)

### UserRepository (domain)
```java
public interface UserRepository {
    Mono<User> findById(Long id);
    Mono<User> findByUsername(String username);
    Mono<User> findByEmail(String email);
    Flux<User> findAll();
    Mono<User> save(User user);
    Mono<Void> deleteById(Long id);
}
```

### IncidentRepository (domain)
```java
public interface IncidentRepository {
    Mono<Incident> findById(Long id);
    Flux<Incident> findAll();
    Flux<Incident> findByStatus(IncidentStatus status);
    Flux<Incident> findByType(IncidentType type);
    Mono<Incident> save(Incident incident);
    Mono<Void> deleteById(Long id);
    Mono<Long> countByStatus(IncidentStatus status);
    Flux<Incident> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
```

---

**FASE 2 COMPLETADA.**

Rutas para abrir los proyectos:
- **Backend**: `C:\Users\Maria\Projects\CityPulse\backend`
- **Frontend**: `C:\Users\Maria\Projects\CityPulse\frontend`

¿Apruebas la arquitectura para continuar con la **FASE 3: MODELO DE DOMINIO**?

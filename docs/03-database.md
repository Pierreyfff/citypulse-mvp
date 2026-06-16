# FASE 4: BASE DE DATOS - CityPulse

## 1. MODELO ENTIDAD-RELACIÓN (ERD)

```
┌────────────────────────────────────────────────────────────────────┐
│                           CITYPULSE DB                              │
│                                                                     │
│  ┌──────────────────────────┐     ┌──────────────────────────────┐  │
│  │         users            │     │         incidents            │  │
│  ├──────────────────────────┤     ├──────────────────────────────┤  │
│  │ id            BIGSERIAL PK│     │ id               BIGSERIAL PK│  │
│  │ username      VARCHAR(50)│────→│ type             VARCHAR(30) │  │
│  │ email         VARCHAR(100)│     │ status           VARCHAR(30) │  │
│  │ password      VARCHAR(255)│     │ description      TEXT        │  │
│  │ role          VARCHAR(20) │     │ address          VARCHAR(255)│  │
│  │ active        BOOLEAN    │     │ latitude         DOUBLE      │  │
│  │ created_at    TIMESTAMP  │     │ longitude        DOUBLE      │  │
│  │ updated_at    TIMESTAMP  │     │ severity         INT         │  │
│  └──────────────────────────┘     │ sensitive_zone   BOOLEAN     │  │
│          │  1                     │ risk_score       INT         │  │
│          │                        │ user_id          BIGINT FK   │  │
│          │                        │ created_at       TIMESTAMP   │  │
│          │                        │ updated_at       TIMESTAMP   │  │
│          │                        └──────────────────────────────┘  │
│          │                                                          │
│          │  1                                                       │
│  ┌──────────────────────────┐                                       │
│  │     refresh_tokens       │                                       │
│  ├──────────────────────────┤                                       │
│  │ id            BIGSERIAL PK│                                      │
│  │ token         VARCHAR(500)│                                      │
│  │ user_id       BIGINT FK  │                                      │
│  │ expires_at    TIMESTAMP  │                                      │
│  │ created_at    TIMESTAMP  │                                      │
│  └──────────────────────────┘                                       │
└────────────────────────────────────────────────────────────────────┘
```

## 2. RELACIONES

| Desde | Hacia | Tipo | Regla |
|-------|-------|------|-------|
| users.id | incidents.user_id | 1:N | Un usuario reporta muchos incidentes |
| users.id | refresh_tokens.user_id | 1:N | Un usuario tiene muchos tokens |

## 3. ÍNDICES

| Índice | Columna | Propósito |
|--------|---------|-----------|
| idx_incidents_status | status | Filtrar incidentes por estado |
| idx_incidents_type | type | Filtrar incidentes por tipo |
| idx_incidents_user_id | user_id | Buscar incidentes de un usuario |
| idx_incidents_created_at | created_at | Ordenar por fecha |
| idx_refresh_tokens_token | token | Buscar refresh token |
| idx_users_username | username | Login por username |
| idx_users_email | email | Login por email |

## 4. TIPOS DE DATOS

| Tipo PostgreSQL | Uso |
|-----------------|-----|
| BIGSERIAL | IDs autoincrementales |
| VARCHAR(50/100/255/500) | Texto de longitud variable |
| TEXT | Descripciones largas |
| DOUBLE PRECISION | Coordenadas geográficas |
| INT | Números enteros (severity, risk_score) |
| BOOLEAN | Flags (active, sensitive_zone) |
| TIMESTAMP | Fechas con zona horaria |

## 5. USO DE PROGRAMACIÓN FUNCIONAL EN MAPEOS

Los mapeadores `UserEntityMapper` e `IncidentEntityMapper` usan:

- **Optional** para null safety en campos opcionales
- **Method References** en lambdas de seteo
- **Funciones puras** sin efectos secundarios

---

**FASE 4 COMPLETADA.**

Archivos creados:
- `backend/src/main/resources/schema.sql` → DDL completo con índices y datos iniciales
- `infrastructure/persistence/entity/UserEntity.java`
- `infrastructure/persistence/entity/IncidentEntity.java`
- `infrastructure/persistence/entity/RefreshTokenEntity.java`
- `infrastructure/persistence/repository/ReactiveUserRepository.java`
- `infrastructure/persistence/repository/ReactiveIncidentRepository.java`
- `infrastructure/persistence/repository/ReactiveRefreshTokenRepository.java`
- `infrastructure/persistence/mapper/UserEntityMapper.java`
- `infrastructure/persistence/mapper/IncidentEntityMapper.java`
- `docker/docker-compose.yml` → Base de datos PostgreSQL 16
- `docs/03-database.md`

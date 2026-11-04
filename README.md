# spa-catalog-service

Carta de servicios, equipo de terapeutas y blogs del **SPA del Bosque**. Spring Boot 3.5 · Java 17 · MySQL.

## Carta 2026

Al iniciar, `CartaSeeder` sincroniza la **Carta de Servicios 2026** (28 servicios con precios, duraciones y descripciones):

- Crea los servicios que faltan y actualiza los que vienen de una carta anterior (`cartaVersion`).
- **No pisa** ediciones hechas luego desde el panel admin.
- Desactiva (no borra) los servicios antiguos que ya no están en la carta, para conservar el historial de ventas.
- Crea el equipo inicial de terapeutas si la tabla está vacía.

Para una nueva carta: editar la lista en `seed/CartaSeeder.java` y subir `CARTA_VERSION`. Se puede desactivar con `CATALOG_SEED_ENABLED=false`.

## Endpoints (`/api/v1/catalog`)

| Recurso | GET | Escritura |
|---|---|---|
| `/servicios`, `/servicios/{id}`, `/servicios/sku/{sku}` | público (solo activos; ADMIN ve todos) | ADMIN: POST, PUT, PATCH `/{id}/estado`, DELETE |
| `/terapeutas` | público (activas; ADMIN todas) | ADMIN: POST, PUT, PATCH `/{id}/estado` |
| `/blogs` | público (publicados; ADMIN también borradores) | ADMIN: POST, PUT, DELETE, PATCH `/{id}/estado`, `/{id}/destacado` |

Cada servicio indica `requiereTerapeuta` (falso para circuito/sauna, que se controlan por aforo) y `personas` (1, 2 o 4).

## Variables de entorno

`JWT_SECRET` (obligatoria, igual en todos los servicios), `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `DB_SSL`, `PORT`/`SERVER_PORT` (8082), `DDL_AUTO`, `CATALOG_SEED_ENABLED`, `SWAGGER_ENABLED`.

## Desarrollo local

```bash
export SPRING_PROFILES_ACTIVE=h2
mvn spring-boot:run
mvn test
```

# BRIVET — Guía de integración API (Solicitud)

Referencia rápida para el frontend. Cada endpoint indica el request exacto en
`BRIVET_API.postman_collection.json` para probarlo antes de integrarlo.

**Base URL:** `http://localhost:8080` (local) · `https://brivetvzla.com/api` (producción)

---

## 1. Login (obtener token)

```
POST /auth/login
```
📎 Postman: **Auth → Login**

**Body:**
```json
{ "email": "vet@brivetvzla.com", "password": "demo1234" }
```

**Response 200:**
```json
{
  "usuario": { "id": 1, "nombre": "Vet", "apellido": "Demo", "email": "...", "role": {...} },
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Guarda `token` y mándalo en cada endpoint de `/vet/**` como:
```
Authorization: Bearer {token}
```

Sin token válido → `401`. Con token pero rol incorrecto → `403`.
📎 Postman: **Auth → Login - credenciales inválidas (401)**

---

## 2. Endpoints públicos (sin login) — `/solicitud`

### Crear un reporte
```
POST /solicitud
```
📎 Postman: **Solicitud (Público) → Crear Solicitud - PERDIDA (perro)** / **Crear Solicitud - ENCONTRADA (gato)**

`multipart/form-data` con 2 partes:
- `data` (Content-Type: `application/json`) — el objeto del reporte
- `fotos` (opcional) — uno o más archivos de imagen

```json
{
  "tipoSolicitud": "PERDIDA",
  "animal": {
    "nombre": "Luna",
    "especie": "PERRO",
    "raza": "Mestizo",
    "color": "Marrón y blanco",
    "tamanio": "MEDIANO",
    "sexo": "HEMBRA",
    "edadAproximada": 3,
    "descripcion": "...",
    "requiereAtencionMedica": false
  },
  "ubicacion": {
    "estadoId": 1,
    "ciudad": "Maracay",
    "direccion": "...",
    "referencia": "...",
    "latitud": 10.246,
    "longitud": -67.596
  },
  "contacto": {
    "nombre": "Ana",
    "apellido": "Martins",
    "email": "ana@example.com",
    "telefono": "04141234567",
    "whatsapp": "04141234567",
    "metodoContacto": "WHATSAPP",
    "permitirDatosPublicos": true
  }
}
```

**Response 200:** el objeto `Solicitud` completo creado (ver forma completa en la sección 5).

---

### Buscar reportes
```
GET /solicitud/search
```
📎 Postman: **Solicitud (Público)** → todos los requests que empiezan con **Buscar -**

| Query param | Obligatorio | Valores | Notas |
|---|---|---|---|
| `tipo` | No | `PERDIDA`, `ENCONTRADA` | Si se omite, mezcla ambos tipos ordenados por fecha — **usar así para "Reportes recientes" del home** |
| `especie` | No | `PERRO`, `GATO` | Si se omite, trae todas las especies — **usar así para la pestaña "Todos"** |
| `estadoId` | No | id numérico | Filtra por estado venezolano |
| `ciudad` | No | texto | Búsqueda parcial, case-insensitive |

Ejemplos:
- Home: `GET /solicitud/search` → 📎 **Buscar - Home (mezclado, sin tipo)**
- Tab "Todos" de Perdidas: `GET /solicitud/search?tipo=PERDIDA` → 📎 **Buscar - Perdidas (Todos)**
- Tab "Perro": `GET /solicitud/search?tipo=PERDIDA&especie=PERRO` → 📎 **Buscar - Perdidas + Perro**
- Tab "Gato": `GET /solicitud/search?tipo=PERDIDA&especie=GATO` → 📎 **Buscar - Perdidas + Gato**
- Por ciudad: `GET /solicitud/search?tipo=PERDIDA&ciudad=Maracay` → 📎 **Buscar - filtro por ciudad**

⚠️ **No mandar `especie=Todos` ni ningún string que no sea `PERRO`/`GATO`** — el backend lo rechaza con `400`. Para "todas las especies", simplemente omite el parámetro.
📎 Postman: **Buscar - especie inválida (debe dar 400)** (para confirmar este comportamiento)

**Response 200:** array de `Solicitud` (ver forma completa en la sección 5). Nunca incluye estados `RECHAZADA` ni `ELIMINADA`.

---

### Detalle de un reporte
```
GET /solicitud/{id}
```
📎 Postman: **Solicitud (Público) → Detalle por id (público)**

Página de detalle de una mascota perdida/encontrada. Igual que `/search`, nunca devuelve solicitudes `RECHAZADA` ni `ELIMINADA` (da `404` en esos casos).
📎 Postman: **Detalle por id - rechazada/eliminada (debe dar 404)**

---

## 3. Endpoints del dashboard veterinario (requieren login) — `/vet/solicitud`

Todos requieren header `Authorization: Bearer {token}`. Rol `ADMIN` o `VET`.

### Listado (pantalla principal del dashboard, después del login)
```
GET /vet/solicitud
GET /vet/solicitud?estado=PENDIENTE
```
📎 Postman: **Solicitud (Vet - Protegido) → Listar todas (dashboard, sin filtro)** / **Listar - solo PENDIENTE**

A diferencia de `/solicitud/search`, **no excluye ningún estado** — trae pendientes, rechazadas y eliminadas también, porque el veterinario necesita gestionarlas.

| Query param | Obligatorio | Valores |
|---|---|---|
| `estado` | No | `PENDIENTE`, `RECHAZADA`, `ACTIVA`, `REUNIDA`, `ADOPTADA`, `ELIMINADA` |

Sin token → `401`/`403`. 📎 Postman: **Listar - sin token (debe dar 401/403)**

### Detalle
```
GET /vet/solicitud/{id}
```
📎 Postman: **Solicitud (Vet - Protegido) → Detalle por id (vet, con observacionVet)**

### Actualizar estatus
```
PUT /vet/solicitud/{id}
```
📎 Postman: **PUT - Aprobar (ACTIVA)** / **PUT - Rechazar (RECHAZADA)** / **PUT - Marcar Reunida (REUNIDA)** / **PUT - Marcar Adoptada (ADOPTADA)**

**Body:**
```json
{ "estado": "ACTIVA", "observacionVet": "Reporte verificado, se ve consistente." }
```
- `estado`: obligatorio. Uno de `PENDIENTE`, `RECHAZADA`, `ACTIVA`, `REUNIDA`, `ADOPTADA`, `ELIMINADA`.
- `observacionVet`: opcional. Si se omite, no se toca el valor que ya tenía.

Casos de error:
- Sin token → `401`/`403` → 📎 **PUT - sin token (debe dar 401/403)**
- Id inexistente → `404` → 📎 **PUT - id inexistente (debe dar 404)**

### Eliminar (soft delete)
```
DELETE /vet/solicitud/{id}
```
📎 Postman: **Solicitud (Vet - Protegido) → DELETE - eliminar (soft delete)**

**No borra nada físicamente** — marca `estado = ELIMINADA` en la solicitud y propaga el mismo flag internamente a `animal`, `contacto` y `ubicacion` asociados (esto último no es visible en el JSON, es solo bookkeeping interno). Las fotos en S3 tampoco se tocan. Devuelve `200` con la solicitud actualizada.

Casos de error:
- Sin token → 📎 **DELETE - sin token (debe dar 401/403)**
- Id inexistente → 📎 **DELETE - id inexistente (debe dar 404)**

---

## 4. Manejo de errores (aplica a todos los endpoints)

| Status | Cuándo | Forma del body |
|---|---|---|
| `400` | Parámetro/body inválido (ej. `especie=OTRO`) | `{ "timestamp": "...", "message": "...", "details": "...", "correlationId": "..." }` |
| `401` | Falta token o token inválido | idem |
| `403` | Token válido pero rol sin permiso | idem |
| `404` | Id no existe (o, en endpoints públicos, existe pero está rechazado/eliminado) | idem |
| `500` | Error inesperado del servidor | idem |

---

## 5. Forma completa de un objeto `Solicitud`

Es la misma forma en **todos** los endpoints (público y vet) — el backend no oculta ningún campo según quién llama; el FE decide qué mostrar en cada pantalla.

```json
{
  "id": 4,
  "animal": {
    "id": 4,
    "refugio": null,
    "tipoReporte": "P",
    "nombre": "Negrito",
    "especie": "P",
    "raza": "Mestizo",
    "color": "Negro",
    "tamanio": "M",
    "sexo": "M",
    "edadAproximada": 3,
    "descripcion": "...",
    "estadoRevision": "P",
    "estadoAnimal": "A",
    "ubicacionTexto": "Caracas, Distrito Capital",
    "telefono": "04141234567",
    "createdAt": "2026-07-01T01:03:07",
    "updatedAt": "2026-07-01T01:03:07"
  },
  "contacto": {
    "id": 4,
    "nombre": "Ana",
    "apellido": "Martins",
    "email": "ana@example.com",
    "telefono": "04141234567",
    "whatsapp": "04141234567",
    "metodoContacto": "W",
    "permitirPublico": "S",
    "estadoContacto": "A",
    "createdAt": "...",
    "updatedAt": "..."
  },
  "ubicacion": {
    "id": 4,
    "estado": { "id": 10, "codigoPais": 58, "nombre": "Distrito Capital", "estado": "A", "createdAt": "...", "updatedAt": "..." },
    "ciudad": "Caracas",
    "sector": "...",
    "direccion": "...",
    "referencia": "...",
    "codigoPostal": null,
    "latitud": 10.246,
    "longitud": -67.596,
    "estadoRegistro": "A",
    "createdAt": "...",
    "updatedAt": "..."
  },
  "tipo": "P",
  "fechaEvento": "2026-07-01T01:03:07",
  "estado": "A",
  "observacionVet": "Reporte verificado, se ve consistente.",
  "mainPhotoUrl": "https://brivetvzla-fotos-dev.s3.amazonaws.com/solicitudes/00000004/foto-1.jpg",
  "createdAt": "2026-07-01T01:03:07",
  "updatedAt": "2026-07-01T01:04:18"
}
```

⚠️ **`observacionVet` siempre viaja en el JSON, incluso en `/solicitud/search` y `/solicitud/{id}` públicos.** El backend no lo oculta — es responsabilidad del FE no pintarlo en las pantallas públicas (home, lista de perdidos/encontrados, detalle público). Sí debe mostrarse en el dashboard veterinario (`/vet/**`).

### Diccionario de códigos de un carácter

| Campo | Valores |
|---|---|
| `solicitud.tipo` | `P`=Perdida, `E`=Encontrada |
| `solicitud.estado` | `P`=Pendiente, `R`=Rechazada, `A`=Activa, `C`=Reunida, `T`=Adoptada, `E`=Eliminada |
| `animal.especie` | `P`=Perro, `G`=Gato, `null`=Otro |
| `animal.tamanio` | `P`=Pequeño, `M`=Mediano, `G`=Grande |
| `animal.sexo` | `M`=Macho, `H`=Hembra, `null`=No sabe |
| `contacto.metodoContacto` | `W`=WhatsApp, `P`=Teléfono, `E`=Email, `A`=Cualquiera |
| `*.estado*` internos (`estadoAnimal`, `estadoContacto`, `estadoRegistro`) | `A`=Activo, `E`=Eliminado — bookkeeping interno, el FE puede ignorarlos |

---

## 6. Checklist rápido de integración

- [ ] Home / "Reportes recientes" → `GET /solicitud/search` (sin `tipo`)
- [ ] Tab "Mascotas Perdidas" → `GET /solicitud/search?tipo=PERDIDA` (+ `especie` si aplica)
- [ ] Tab "Mascotas Encontradas" → `GET /solicitud/search?tipo=ENCONTRADA`
- [ ] Página de detalle pública → `GET /solicitud/{id}`
- [ ] Formulario "Reportar mascota" → `POST /solicitud` (multipart)
- [ ] Login del veterinario → `POST /auth/login`, guardar `token`
- [ ] Dashboard vet (listado) → `GET /vet/solicitud` (+ `estado` si hay filtro)
- [ ] Dashboard vet (detalle) → `GET /vet/solicitud/{id}`
- [ ] Aprobar/Rechazar/Reunida/Adoptada → `PUT /vet/solicitud/{id}`
- [ ] Eliminar solicitud → `DELETE /vet/solicitud/{id}`

Importa `BRIVET_API.postman_collection.json` en Postman para probar cualquiera de estos antes de escribir el código — cada request de esta guía tiene su 📎 correspondiente ahí, con ejemplos reales y checks de error incluidos (401/403/404/400).

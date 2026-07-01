package com.brivetvzla.backend.controller;

import com.brivetvzla.backend.model.dto.request.VetSolicitudUpdateRequest;
import com.brivetvzla.backend.model.entity.Solicitud;
import com.brivetvzla.backend.model.enums.EstadoSolicitud;
import com.brivetvzla.backend.service.SolicitudService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints protegidos del dashboard veterinario para gestionar solicitudes.
 * Todo lo bajo /vet/** requiere JWT válido con rol ADMIN o VET
 * (ver SecurityConfig + JwtAuthFilter).
 *
 * Devuelven la entidad Solicitud completa, igual que /solicitud/** público —
 * el backend no filtra observacionVet en ningún lado; el FE decide qué
 * mostrar según la pantalla.
 */
@RestController
@RequestMapping("/vet/solicitud")
public class VetSolicitudController {

    private final SolicitudService solicitudService;

    public VetSolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    /**
     * Listado de solicitudes para el dashboard veterinario, después del login.
     * A diferencia de la búsqueda pública, no excluye ningún estado — el
     * veterinario ve pendientes, rechazadas y eliminadas también.
     *
     * Ejemplos:
     *   GET /vet/solicitud                    → todas, sin filtro
     *   GET /vet/solicitud?estado=PENDIENTE   → solo las que necesitan revisión
     *
     * @param estado opcional: PENDIENTE, RECHAZADA, ACTIVA, REUNIDA, ADOPTADA, ELIMINADA
     */
    @GetMapping
    public ResponseEntity<List<Solicitud>> listSolicitudes(
            @RequestParam(required = false) EstadoSolicitud estado) {

        List<Solicitud> solicitudes = solicitudService.listSolicitudesForVet(estado);
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Detalle de una solicitud para el dashboard veterinario.
     * A diferencia del detalle público (GET /solicitud/{id}), no excluye
     * ningún estado (rechazadas y eliminadas también son visibles aquí).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> getSolicitudById(@PathVariable Integer id) {
        Solicitud solicitud = solicitudService.getSolicitudForVetById(id);
        return ResponseEntity.ok(solicitud);
    }

    /**
     * Actualiza el estatus (y opcionalmente la observación) de una solicitud.
     * Ej. aprobar (A), rechazar (R), marcar reunida (C) o adoptada (T).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Solicitud> updateSolicitud(
            @PathVariable Integer id,
            @RequestBody @Valid VetSolicitudUpdateRequest request) {

        Solicitud actualizada = solicitudService.updateEstadoSolicitud(id, request);
        return ResponseEntity.ok(actualizada);
    }

    /**
     * Elimina lógicamente una solicitud (soft delete: estado -> ELIMINADA).
     * No borra la fila ni los registros asociados (animal/contacto/ubicacion/fotos).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Solicitud> deleteSolicitud(@PathVariable Integer id) {
        Solicitud eliminada = solicitudService.deleteSolicitud(id);
        return ResponseEntity.ok(eliminada);
    }
}

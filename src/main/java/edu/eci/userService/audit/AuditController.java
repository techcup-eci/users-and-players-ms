package edu.eci.userService.audit;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller REST para consultar los registros de auditoría.
 *
 * Rutas expuestas (todas bajo /audit):
 *   GET /audit                        → todos los registros
 *   GET /audit/entity/{entityType}    → por tipo de entidad
 *   GET /audit/entity/{entityType}/{entityId} → por entidad e ID
 *   GET /audit/action/{action}        → por acción lógica
 *   GET /audit/status/{status}        → por estado (SUCCESS/ERROR)
 *   GET /audit/range?from=…&to=…     → por rango de fechas (ISO 8601)
 */

@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los registros de auditoría")
    public List<AuditLog> getAll() {
        return auditService.getAll();
    }

    @GetMapping("/entity/{entityType}")
    @Operation(summary = "Registros por tipo de entidad (User, AthleticProfile)")
    public List<AuditLog> getByEntityType(@PathVariable String entityType) {
        return auditService.getByEntityType(entityType);
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    @Operation(summary = "Registros de un recurso concreto")
    public List<AuditLog> getByEntityAndId(@PathVariable String entityType,
                                            @PathVariable String entityId) {
        return auditService.getByEntityAndId(entityType, entityId);
    }

    @GetMapping("/action/{action}")
    @Operation(summary = "Registros por acción lógica (CREATE_USER, UPDATE_PROFILE…)")
    public List<AuditLog> getByAction(@PathVariable String action) {
        return auditService.getByAction(action);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Registros por estado (SUCCESS / ERROR)")
    public List<AuditLog> getByStatus(@PathVariable String status) {
        return auditService.getByStatus(status);
    }

    @GetMapping("/range")
    @Operation(summary = "Registros entre dos fechas ISO-8601",
               description = "Ejemplo: /audit/range?from=2025-01-01T00:00:00&to=2025-12-31T23:59:59")
    public List<AuditLog> getByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return auditService.getByDateRange(from, to);
    }
}

package edu.eci.userService.audit;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de auditoría.
 * Encapsula la lógica de persistencia de los registros de auditoría y
 * expone métodos de consulta para el controller de auditoría.
 */
@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    /**
     * Persiste un registro de auditoría.
     *
     * @param action      nombre lógico de la operación
     * @param httpMethod  verbo HTTP
     * @param endpoint    ruta invocada
     * @param entityType  "User" o "AthleticProfile"
     * @param entityId    ID del recurso (puede ser null)
     * @param performedBy IP del cliente
     * @param status      "SUCCESS" o "ERROR"
     * @param detail      detalle adicional
     */
    public AuditLog log(AuditLogRequest request) {
        AuditLog entry = new AuditLog(
                request.getAction(),
                request.getHttpMethod(),
                request.getEndpoint(),
                request.getEntityType(),
                request.getEntityId(),
                request.getPerformedBy(),
                request.getStatus(),
                request.getDetail()
        );
        return auditLogRepository.save(entry);
    }

    // ── Consultas ─────────────────────────────────────────────────────────────

    public List<AuditLog> getAll() {
        return auditLogRepository.findAll();
    }

    public List<AuditLog> getByEntityType(String entityType) {
        return auditLogRepository.findByEntityType(entityType);
    }

    public List<AuditLog> getByEntityAndId(String entityType, String entityId) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }

    public List<AuditLog> getByAction(String action) {
        return auditLogRepository.findByAction(action);
    }

    public List<AuditLog> getByStatus(String status) {
        return auditLogRepository.findByStatus(status);
    }

    public List<AuditLog> getByDateRange(LocalDateTime from, LocalDateTime to) {
        return auditLogRepository.findByTimestampBetween(from, to);
    }
}

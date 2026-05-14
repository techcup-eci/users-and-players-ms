package edu.eci.userService.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para los registros de auditoría.
 * Provee búsquedas útiles para consultas administrativas.
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    /** Todos los registros de un tipo de entidad (User / AthleticProfile). */
    List<AuditLog> findByEntityType(String entityType);

    /** Registros de un recurso concreto. */
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, String entityId);

    /** Registros por acción lógica (CREATE_USER, UPDATE_PROFILE…). */
    List<AuditLog> findByAction(String action);

    /** Registros entre dos fechas — útil para reportes por rango. */
    List<AuditLog> findByTimestampBetween(LocalDateTime from, LocalDateTime to);

    /** Registros fallidos. */
    List<AuditLog> findByStatus(String status);
}

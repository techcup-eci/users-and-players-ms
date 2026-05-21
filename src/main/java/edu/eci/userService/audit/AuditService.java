package edu.eci.userService.audit;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

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

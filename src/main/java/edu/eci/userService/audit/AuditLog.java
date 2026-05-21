package edu.eci.userService.audit;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String action;

    @Column(nullable = false, length = 10)
    private String httpMethod;

    @Column(nullable = false, length = 255)
    private String endpoint;

    @Column(nullable = false, length = 30)
    private String entityType;

    @Column
    private String entityId;

    @Column(nullable = false, length = 50)
    private String performedBy;

    @Column(nullable = false, length = 10)
    private String status;

    @Column(length = 500)
    private String detail;

    @Column(nullable = false)
    private LocalDateTime timestamp;


    public AuditLog() {
        // Sin cuerpo: requerido por JPA para entidades @Entity.
    }

    public AuditLog(String action, String httpMethod, String endpoint,
                    String entityType, String entityId, String performedBy,
                    String status, String detail) {
        this.action = action;
        this.httpMethod = httpMethod;
        this.endpoint = endpoint;
        this.entityType = entityType;
        this.entityId = entityId;
        this.performedBy = performedBy;
        this.status = status;
        this.detail = detail;
        this.timestamp = LocalDateTime.now();
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }

    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }

    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}

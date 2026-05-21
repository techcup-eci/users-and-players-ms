package edu.eci.userService.audit;

public class AuditLogRequest {

    private String action;
    private String httpMethod;
    private String endpoint;
    private String entityType;
    private String entityId;
    private String performedBy;
    private String status;
    private String detail;

    public AuditLogRequest() {
    }

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
}

package edu.eci.userService.audit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class AuditLogTest {

    @Test
    @DisplayName("AuditLog: Constructor and Getters/Setters")
    void auditLogTest() {
        LocalDateTime now = LocalDateTime.now();
        AuditLog log = new AuditLog("ACT", "GET", "/uri", "User", "1", "127.0.0.1", "SUCCESS", "detail");
        
        assertThat(log.getAction()).isEqualTo("ACT");
        assertThat(log.getHttpMethod()).isEqualTo("GET");
        assertThat(log.getEndpoint()).isEqualTo("/uri");
        assertThat(log.getEntityType()).isEqualTo("User");
        assertThat(log.getEntityId()).isEqualTo("1");
        assertThat(log.getPerformedBy()).isEqualTo("127.0.0.1");
        assertThat(log.getStatus()).isEqualTo("SUCCESS");
        assertThat(log.getDetail()).isEqualTo("detail");
        assertThat(log.getTimestamp()).isAfterOrEqualTo(now);

        log.setId(10L);
        assertThat(log.getId()).isEqualTo(10L);
        
        LocalDateTime then = LocalDateTime.now().plusDays(1);
        log.setTimestamp(then);
        assertThat(log.getTimestamp()).isEqualTo(then);
        
        log.setAction("NEW");
        assertThat(log.getAction()).isEqualTo("NEW");
        
        log.setHttpMethod("POST");
        assertThat(log.getHttpMethod()).isEqualTo("POST");
        
        log.setEndpoint("/new");
        assertThat(log.getEndpoint()).isEqualTo("/new");
        
        log.setEntityType("Type");
        assertThat(log.getEntityType()).isEqualTo("Type");
        
        log.setEntityId("2");
        assertThat(log.getEntityId()).isEqualTo("2");
        
        log.setPerformedBy("IP");
        assertThat(log.getPerformedBy()).isEqualTo("IP");
        
        log.setStatus("ERROR");
        assertThat(log.getStatus()).isEqualTo("ERROR");
        
        log.setDetail("new detail");
        assertThat(log.getDetail()).isEqualTo("new detail");
        
        AuditLog emptyLog = new AuditLog();
        assertThat(emptyLog).isNotNull();
    }
}

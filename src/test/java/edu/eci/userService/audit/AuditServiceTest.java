package edu.eci.userService.audit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    private AuditService auditService;

    @BeforeEach
    void setUp() {
        auditService = new AuditService(auditLogRepository);
    }

    @Test
    @DisplayName("Debe persistir un log de auditoría")
    void logTest() {
        AuditLog log = new AuditLog();
        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(log);

        AuditLogRequest request = new AuditLogRequest();
        request.setAction("ACT");
        request.setHttpMethod("GET");
        request.setEndpoint("/uri");
        request.setEntityType("User");
        request.setEntityId("1");
        request.setPerformedBy("IP");
        request.setStatus("SUCCESS");
        request.setDetail(null);

        AuditLog result = auditService.log(request);

        assertThat(result).isEqualTo(log);
        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    @DisplayName("Debe retornar todos los logs")
    void getAllTest() {
        when(auditLogRepository.findAll()).thenReturn(Collections.emptyList());
        List<AuditLog> result = auditService.getAll();
        assertThat(result).isEmpty();
        verify(auditLogRepository).findAll();
    }

    @Test
    @DisplayName("Debe filtrar por tipo de entidad")
    void getByEntityTypeTest() {
        when(auditLogRepository.findByEntityType("User")).thenReturn(Collections.emptyList());
        List<AuditLog> result = auditService.getByEntityType("User");
        assertThat(result).isEmpty();
        verify(auditLogRepository).findByEntityType("User");
    }

    @Test
    @DisplayName("Debe filtrar por entidad e ID")
    void getByEntityAndIdTest() {
        when(auditLogRepository.findByEntityTypeAndEntityId("User", "1")).thenReturn(Collections.emptyList());
        List<AuditLog> result = auditService.getByEntityAndId("User", "1");
        assertThat(result).isEmpty();
        verify(auditLogRepository).findByEntityTypeAndEntityId("User", "1");
    }

    @Test
    @DisplayName("Debe filtrar por acción")
    void getByActionTest() {
        when(auditLogRepository.findByAction("ACT")).thenReturn(Collections.emptyList());
        List<AuditLog> result = auditService.getByAction("ACT");
        assertThat(result).isEmpty();
        verify(auditLogRepository).findByAction("ACT");
    }

    @Test
    @DisplayName("Debe filtrar por estado")
    void getByStatusTest() {
        when(auditLogRepository.findByStatus("SUCCESS")).thenReturn(Collections.emptyList());
        List<AuditLog> result = auditService.getByStatus("SUCCESS");
        assertThat(result).isEmpty();
        verify(auditLogRepository).findByStatus("SUCCESS");
    }

    @Test
    @DisplayName("Debe filtrar por rango de fechas")
    void getByDateRangeTest() {
        LocalDateTime now = LocalDateTime.now();
        when(auditLogRepository.findByTimestampBetween(any(), any())).thenReturn(Collections.emptyList());
        List<AuditLog> result = auditService.getByDateRange(now, now);
        assertThat(result).isEmpty();
        verify(auditLogRepository).findByTimestampBetween(now, now);
    }
}

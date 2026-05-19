package edu.eci.userService.audit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuditController.class)
class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditService auditService;

    @Test
    @DisplayName("GET /audit")
    void getAllTest() throws Exception {
        when(auditService.getAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/audit")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /audit/entity/{type}")
    void getByTypeTest() throws Exception {
        when(auditService.getByEntityType("User")).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/audit/entity/User")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /audit/entity/{type}/{id}")
    void getByTypeAndIdTest() throws Exception {
        when(auditService.getByEntityAndId("User", "1")).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/audit/entity/User/1")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /audit/action/{action}")
    void getByActionTest() throws Exception {
        when(auditService.getByAction("ACT")).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/audit/action/ACT")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /audit/status/{status}")
    void getByStatusTest() throws Exception {
        when(auditService.getByStatus("SUCCESS")).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/audit/status/SUCCESS")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /audit/range")
    void getByRangeTest() throws Exception {
        when(auditService.getByDateRange(any(), any())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/audit/range")
                        .param("from", "2025-01-01T00:00:00")
                        .param("to", "2025-12-31T23:59:59"))
                .andExpect(status().isOk());
    }
}

package edu.eci.userService.audit;

import edu.eci.userService.controller.UserController;
import edu.eci.userService.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditAspectTest {

    @Mock
    private AuditService auditService;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private MethodSignature methodSignature;

    @Mock
    private HttpServletRequest httpServletRequest;

    private AuditAspect auditAspect;

    @BeforeEach
    void setUp() {
        auditAspect = new AuditAspect(auditService);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    @DisplayName("Debe auditar correctamente una operacion exitosa (Create)")
    void auditSuccessTest() throws Throwable {
        Method method = UserController.class.getMethod("createUser", UserDTO.class);
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{new UserDTO()});
        when(proceedingJoinPoint.proceed()).thenReturn("result");
        when(httpServletRequest.getMethod()).thenReturn("POST");
        when(httpServletRequest.getRequestURI()).thenReturn("/api/users");
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        Object result = auditAspect.auditUser(proceedingJoinPoint);

        assertThat(result).isEqualTo("result");
        ArgumentCaptor<AuditLogRequest> captor = ArgumentCaptor.forClass(AuditLogRequest.class);
        verify(auditService).log(captor.capture());
        AuditLogRequest req = captor.getValue();

        assertThat(req.getAction()).isEqualTo("CREATE_USER");
        assertThat(req.getHttpMethod()).isEqualTo("POST");
        assertThat(req.getEndpoint()).isEqualTo("/api/users");
        assertThat(req.getEntityType()).isEqualTo("User");
        assertThat(req.getPerformedBy()).isEqualTo("127.0.0.1");
        assertThat(req.getStatus()).isEqualTo("SUCCESS");
        assertThat(req.getDetail()).isNull();
    }

    @Test
    @DisplayName("Debe auditar correctamente una operacion con ID (GetById)")
    void auditWithIdTest() throws Throwable {
        Method method = UserController.class.getMethod("getUserById", long.class);
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{1L});
        when(proceedingJoinPoint.proceed()).thenReturn("result");
        when(httpServletRequest.getMethod()).thenReturn("GET");
        when(httpServletRequest.getRequestURI()).thenReturn("/api/users/1");
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        auditAspect.auditUser(proceedingJoinPoint);

        ArgumentCaptor<AuditLogRequest> captor = ArgumentCaptor.forClass(AuditLogRequest.class);
        verify(auditService).log(captor.capture());
        AuditLogRequest req = captor.getValue();

        assertThat(req.getEntityId()).isEqualTo("1");
    }

    @Test
    @DisplayName("Debe auditar correctamente una operacion fallida")
    void auditErrorTest() throws Throwable {
        Method method = UserController.class.getMethod("deleteUser", long.class);
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{99L});
        when(proceedingJoinPoint.proceed()).thenThrow(new RuntimeException("Test Exception"));
        when(httpServletRequest.getMethod()).thenReturn("DELETE");
        when(httpServletRequest.getRequestURI()).thenReturn("/api/users/99");
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        assertThatThrownBy(() -> auditAspect.auditUser(proceedingJoinPoint))
                .isInstanceOf(RuntimeException.class);

        ArgumentCaptor<AuditLogRequest> captor = ArgumentCaptor.forClass(AuditLogRequest.class);
        verify(auditService).log(captor.capture());
        AuditLogRequest req = captor.getValue();

        assertThat(req.getAction()).isEqualTo("DELETE_USER");
        assertThat(req.getEntityType()).isEqualTo("User");
        assertThat(req.getEntityId()).isEqualTo("99");
        assertThat(req.getStatus()).isEqualTo("ERROR");
        assertThat(req.getDetail()).contains("Test Exception");
    }

    @Test
    @DisplayName("No debe auditar listados generales (getAll)")
    void shouldNotAuditGetAll() throws Throwable {
        Method method = UserController.class.getMethod("getAllUsers");
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(proceedingJoinPoint.proceed()).thenReturn("list");

        Object result = auditAspect.auditUser(proceedingJoinPoint);

        assertThat(result).isEqualTo("list");
        verify(auditService, never()).log(any(AuditLogRequest.class));
    }

    @Test
    @DisplayName("Debe resolver IP desde X-Forwarded-For")
    void resolveIpFromHeaderTest() throws Throwable {
        Method method = UserController.class.getMethod("getUserById", long.class);
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{1L});
        when(proceedingJoinPoint.proceed()).thenReturn("result");
        when(httpServletRequest.getHeader("X-Forwarded-For")).thenReturn("10.0.0.1, 192.168.1.1");
        when(httpServletRequest.getMethod()).thenReturn("GET");
        when(httpServletRequest.getRequestURI()).thenReturn("/api/users/1");

        auditAspect.auditUser(proceedingJoinPoint);

        ArgumentCaptor<AuditLogRequest> captor = ArgumentCaptor.forClass(AuditLogRequest.class);
        verify(auditService).log(captor.capture());
        AuditLogRequest req = captor.getValue();

        assertThat(req.getPerformedBy()).isEqualTo("10.0.0.1");
    }

    @Test
    @DisplayName("Debe manejar metodos desconocidos")
    void unknownMethodActionTest() throws Throwable {
        class Fake { public void customOp() {} }
        Method method = Fake.class.getMethod("customOp");

        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{});
        when(proceedingJoinPoint.proceed()).thenReturn("ok");
        when(httpServletRequest.getMethod()).thenReturn("GET");
        when(httpServletRequest.getRequestURI()).thenReturn("/api/athletic-profiles");
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        auditAspect.auditAthleticProfile(proceedingJoinPoint);

        ArgumentCaptor<AuditLogRequest> captor = ArgumentCaptor.forClass(AuditLogRequest.class);
        verify(auditService).log(captor.capture());
        AuditLogRequest req = captor.getValue();

        assertThat(req.getAction()).isEqualTo("CUSTOMOP_ATHLETICPROFILE");
        assertThat(req.getEntityType()).isEqualTo("AthleticProfile");
    }

    @Test
    @DisplayName("Debe auditar UPDATE con ID")
    void auditUpdateWithIdTest() throws Throwable {
        Method method = UserController.class.getMethod("updateUser", long.class, UserDTO.class);
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{1L, new UserDTO()});
        when(proceedingJoinPoint.proceed()).thenReturn("result");
        when(httpServletRequest.getMethod()).thenReturn("PUT");
        when(httpServletRequest.getRequestURI()).thenReturn("/api/users/1");
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        auditAspect.auditUser(proceedingJoinPoint);

        ArgumentCaptor<AuditLogRequest> captor = ArgumentCaptor.forClass(AuditLogRequest.class);
        verify(auditService).log(captor.capture());
        AuditLogRequest req = captor.getValue();

        assertThat(req.getAction()).isEqualTo("UPDATE_USER");
        assertThat(req.getEntityId()).isEqualTo("1");
        assertThat(req.getStatus()).isEqualTo("SUCCESS");
    }

    @Test
    @DisplayName("Debe manejar ausencia de contexto HTTP")
    void auditWithoutRequestContextTest() throws Throwable {
        RequestContextHolder.resetRequestAttributes();
        Method method = UserController.class.getMethod("createUser", UserDTO.class);
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{new UserDTO()});
        when(proceedingJoinPoint.proceed()).thenReturn("result");

        auditAspect.auditUser(proceedingJoinPoint);

        ArgumentCaptor<AuditLogRequest> captor = ArgumentCaptor.forClass(AuditLogRequest.class);
        verify(auditService).log(captor.capture());
        AuditLogRequest req = captor.getValue();

        assertThat(req.getHttpMethod()).isEqualTo("UNKNOWN");
        assertThat(req.getEndpoint()).isEqualTo("UNKNOWN");
        assertThat(req.getPerformedBy()).isEqualTo("UNKNOWN");
    }
}

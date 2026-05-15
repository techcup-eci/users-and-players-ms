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
import org.mockito.ArgumentMatchers;
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
    @DisplayName("Debe auditar correctamente una operación exitosa (Create)")
    void auditSuccessTest() throws Throwable {
        Method method = UserController.class.getMethod("createUser", UserDTO.class);
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{new UserDTO()});
        when(proceedingJoinPoint.proceed()).thenReturn("result");
        when(httpServletRequest.getMethod()).thenReturn("POST");
        when(httpServletRequest.getRequestURI()).thenReturn("/User");
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        Object result = auditAspect.auditUser(proceedingJoinPoint);

        assertThat(result).isEqualTo("result");
        verify(auditService).log(eq("CREATE_USER"), eq("POST"), eq("/User"), eq("User"), any(), eq("127.0.0.1"), eq("SUCCESS"), isNull());
    }

    @Test
    @DisplayName("Debe auditar correctamente una operación con ID (GetById)")
    void auditWithIdTest() throws Throwable {
        Method method = UserController.class.getMethod("getUserById", long.class);
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{1L});
        when(proceedingJoinPoint.proceed()).thenReturn("result");
        when(httpServletRequest.getMethod()).thenReturn("GET");
        when(httpServletRequest.getRequestURI()).thenReturn("/User/1");

        auditAspect.auditUser(proceedingJoinPoint);

        verify(auditService).log(any(), any(), any(), any(), eq("1"), any(), any(), any());
    }

    @Test
    @DisplayName("Debe auditar correctamente una operación fallida")
    void auditErrorTest() throws Throwable {
        Method method = UserController.class.getMethod("deleteUser", long.class);
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{99L});
        when(proceedingJoinPoint.proceed()).thenThrow(new RuntimeException("Test Exception"));

        assertThatThrownBy(() -> auditAspect.auditUser(proceedingJoinPoint))
                .isInstanceOf(RuntimeException.class);

        verify(auditService).log(eq("DELETE_USER"), any(), any(), eq("User"), eq("99"), any(), eq("ERROR"), contains("Test Exception"));
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
        verify(auditService, never()).log(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("Debe resolver IP desde X-Forwarded-For")
    void resolveIpFromHeaderTest() throws Throwable {
        Method method = UserController.class.getMethod("getUserById", long.class);
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{1L});
        when(httpServletRequest.getHeader("X-Forwarded-For")).thenReturn("10.0.0.1, 192.168.1.1");

        auditAspect.auditUser(proceedingJoinPoint);

        verify(auditService).log(any(), any(), any(), any(), any(), eq("10.0.0.1"), any(), any());
    }

    @Test
    @DisplayName("Debe manejar métodos desconocidos")
    void unknownMethodActionTest() throws Throwable {
        // Creamos un método ficticio para probar el fallback de resolveAction
        class Fake { public void customOp() {} }
        Method method = Fake.class.getMethod("customOp");
        
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{});

        auditAspect.auditAthleticProfile(proceedingJoinPoint);

        verify(auditService).log(eq("CUSTOMOP_ATHLETICPROFILE"), any(), any(), eq("AthleticProfile"), any(), any(), any(), any());
    }
}

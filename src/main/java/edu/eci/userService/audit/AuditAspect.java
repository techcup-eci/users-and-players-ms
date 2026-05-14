package edu.eci.userService.audit;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Aspecto de auditoría (AOP).
 *
 * Intercepta TODAS las operaciones que no sean listados (GET sin parámetro de ID)
 * en los controllers de User y AthleticProfile, y registra:
 *   - Acción lógica derivada del nombre del método.
 *   - Verbo HTTP real obtenido desde el contexto de la request.
 *   - Ruta invocada.
 *   - Tipo y ID de la entidad (extraídos de los argumentos cuando aplique).
 *   - IP del cliente.
 *   - Estado SUCCESS / ERROR.
 *   - Detalle del error si ocurre una excepción.
 *
 * Sólo se auditan las operaciones de escritura y consultas individuales
 * (create, update, delete, getById), no los listados generales.
 */
@Aspect
@Component
public class AuditAspect {

    private final AuditService auditService;

    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    // ── Pointcuts ─────────────────────────────────────────────────────────────

    /** Captura todos los métodos públicos de UserController. */
    @Around("execution(* edu.eci.userService.controller.UserController.*(..))")
    public Object auditUser(ProceedingJoinPoint pjp) throws Throwable {
        return audit(pjp, "User");
    }

    /** Captura todos los métodos públicos de AthleticProfileController. */
    @Around("execution(* edu.eci.userService.controller.AthleticProfileController.*(..))")
    public Object auditAthleticProfile(ProceedingJoinPoint pjp) throws Throwable {
        return audit(pjp, "AthleticProfile");
    }

    // ── Lógica central ───────────────────────────────────────────────────────

    private Object audit(ProceedingJoinPoint pjp, String entityType) throws Throwable {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();

        // Derivar acción lógica a partir del nombre del método
        String action = resolveAction(methodName, entityType);

        // Obtener datos de la request HTTP actual
        HttpServletRequest request = currentRequest();
        String httpMethod  = request != null ? request.getMethod() : "UNKNOWN";
        String endpoint    = request != null ? request.getRequestURI() : "UNKNOWN";
        String performedBy = request != null ? resolveClientIp(request) : "UNKNOWN";

        // Extraer el ID de la entidad de los argumentos (primer argumento Long/long)
        String entityId = resolveEntityId(pjp.getArgs());

        // Saltar auditoría en listados generales (getAll*)
        if (methodName.startsWith("getAll")) {
            return pjp.proceed();
        }

        Object result;
        try {
            result = pjp.proceed();
            auditService.log(action, httpMethod, endpoint, entityType,
                    entityId, performedBy, "SUCCESS", null);
        } catch (Exception ex) {
            auditService.log(action, httpMethod, endpoint, entityType,
                    entityId, performedBy, "ERROR",
                    ex.getClass().getSimpleName() + ": " + ex.getMessage());
            throw ex;
        }
        return result;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Deriva el nombre lógico de la acción auditada a partir del nombre del método
     * del controller y del tipo de entidad.
     */
    private String resolveAction(String methodName, String entityType) {
        String upper = entityType.toUpperCase();
        if (methodName.startsWith("create"))  return "CREATE_"  + upper;
        if (methodName.startsWith("update"))  return "UPDATE_"  + upper;
        if (methodName.startsWith("delete"))  return "DELETE_"  + upper;
        if (methodName.startsWith("getAll"))  return "LIST_"    + upper;
        if (methodName.startsWith("get"))     return "GET_"     + upper;
        return methodName.toUpperCase() + "_" + upper;
    }

    /**
     * Extrae el primer argumento de tipo Long o long como ID de entidad.
     * Si no existe, devuelve null.
     */
    private String resolveEntityId(Object[] args) {
        if (args == null) return null;
        return Arrays.stream(args)
                .filter(a -> a instanceof Long)
                .map(a -> String.valueOf(a))
                .findFirst()
                .orElse(null);
    }

    /** Obtiene la request HTTP del contexto de Spring. */
    private HttpServletRequest currentRequest() {
        try {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attrs.getRequest();
        } catch (IllegalStateException e) {
            return null;
        }
    }

    /**
     * Resuelve la IP real del cliente teniendo en cuenta proxies inversos
     * (cabecera X-Forwarded-For).
     */
    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}

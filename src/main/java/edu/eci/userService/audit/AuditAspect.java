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

@Aspect
@Component
public class AuditAspect {

    private final AuditService auditService;

    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    @Around("execution(* edu.eci.userService.controller.UserController.*(..))")
    public Object auditUser(ProceedingJoinPoint pjp) throws Throwable {
        return audit(pjp, "User");
    }

    @Around("execution(* edu.eci.userService.controller.AthleticProfileController.*(..))")
    public Object auditAthleticProfile(ProceedingJoinPoint pjp) throws Throwable {
        return audit(pjp, "AthleticProfile");
    }

    private Object audit(ProceedingJoinPoint pjp, String entityType) throws Throwable {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();

        String action = resolveAction(methodName, entityType);

        HttpServletRequest request = currentRequest();
        String httpMethod  = request != null ? request.getMethod() : "UNKNOWN";
        String endpoint    = request != null ? request.getRequestURI() : "UNKNOWN";
        String performedBy = request != null ? resolveClientIp(request) : "UNKNOWN";

        String entityId = resolveEntityId(pjp.getArgs());

        if (methodName.startsWith("getAll")) {
            return pjp.proceed();
        }

        Object result;
        try {
            result = pjp.proceed();
            AuditLogRequest auditRequest = new AuditLogRequest();
            auditRequest.setAction(action);
            auditRequest.setHttpMethod(httpMethod);
            auditRequest.setEndpoint(endpoint);
            auditRequest.setEntityType(entityType);
            auditRequest.setEntityId(entityId);
            auditRequest.setPerformedBy(performedBy);
            auditRequest.setStatus("SUCCESS");
            auditRequest.setDetail(null);
            auditService.log(auditRequest);
        } catch (Exception ex) {
            AuditLogRequest auditRequest = new AuditLogRequest();
            auditRequest.setAction(action);
            auditRequest.setHttpMethod(httpMethod);
            auditRequest.setEndpoint(endpoint);
            auditRequest.setEntityType(entityType);
            auditRequest.setEntityId(entityId);
            auditRequest.setPerformedBy(performedBy);
            auditRequest.setStatus("ERROR");
            auditRequest.setDetail(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            auditService.log(auditRequest);
            throw ex;
        }
        return result;
    }

    private String resolveAction(String methodName, String entityType) {
        String upper = entityType.toUpperCase();
        if (methodName.startsWith("create"))  return "CREATE_"  + upper;
        if (methodName.startsWith("update"))  return "UPDATE_"  + upper;
        if (methodName.startsWith("delete"))  return "DELETE_"  + upper;
        if (methodName.startsWith("getAll"))  return "LIST_"    + upper;
        if (methodName.startsWith("get"))     return "GET_"     + upper;
        return methodName.toUpperCase() + "_" + upper;
    }

    private String resolveEntityId(Object[] args) {
        if (args == null) return null;
        return Arrays.stream(args)
                .filter(a -> a instanceof Long)
                .map(a -> String.valueOf(a))
                .findFirst()
                .orElse(null);
    }

    private HttpServletRequest currentRequest() {
        try {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attrs.getRequest();
        } catch (IllegalStateException e) {
            return null;
        }
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}

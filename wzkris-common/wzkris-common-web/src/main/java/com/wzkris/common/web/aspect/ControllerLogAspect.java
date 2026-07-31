package com.wzkris.common.web.aspect;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzkris.common.web.properties.ControllerLogProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 请求统计打印
 *
 * @author wzkris
 */
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
@Aspect
public class ControllerLogAspect {

    private final ObjectMapper objectMapper;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final ControllerLogProperties controllerLogProperties;

    public ControllerLogAspect(ObjectMapper objectMapper, ControllerLogProperties controllerLogProperties) {
        this.controllerLogProperties = controllerLogProperties;
        this.objectMapper = objectMapper;
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Pointcut("within(com.wzkris..*) && bean(*Controller)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes == null ? null : attributes.getRequest();
        if (request == null || shouldIgnore(request.getRequestURI())) {
            return joinPoint.proceed();
        }

        long startTime = System.currentTimeMillis();
        long endTime = 0L;
        Object result = null;

        try {
            result = joinPoint.proceed();
            endTime = System.currentTimeMillis();
        } catch (Exception e) {
            endTime = System.currentTimeMillis();
            throw e;
        } finally {
            log.info("""
                            Request URL: {}
                            Request Method: {}
                            Request Parameters: {}
                            Request Body: {}
                            Response: {}
                            Time Cost: {} ms
                            """,
                    request.getRequestURL(),
                    request.getMethod(),
                    this.getRequestParams(request),
                    this.getRequestBody(joinPoint),
                    this.serializeValue(result),
                    endTime - startTime);
        }

        return result;
    }

    public boolean shouldIgnore(String requestUri) {
        List<String> ignoreUrls = controllerLogProperties.getIgnoreUrls();

        for (String pattern : ignoreUrls) {
            if (pathMatcher.match(pattern, requestUri)) {
                return true;
            }
        }
        return false;
    }

    private String getRequestParams(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        Map<String, String[]> paramMap = request.getParameterMap();
        if (paramMap.isEmpty()) {
            return null;
        }

        return this.serializeValue(paramMap);
    }

    private String getRequestBody(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        java.lang.reflect.Parameter[] parameters = signature.getMethod().getParameters();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameters.length; i++) {
            java.lang.reflect.Parameter parameter = parameters[i];
            // 检查参数是否被 @RequestBody 注解标注
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                Object arg = args[i];
                // 排除 HttpServletRequest 和 MultipartFile
                if (!isFilterObject(arg)) {
                    return this.serializeValue(arg);
                }
            }
        }

        return null;
    }

    private String serializeValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof CharSequence || value instanceof Number || value instanceof Boolean || value instanceof Enum<?>) {
            return String.valueOf(value);
        }

        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            log.warn("ControllerStatisticAspect serialize value failed: {}", e.getMessage());
            return String.valueOf(value);
        }
    }

    private boolean isFilterObject(Object value) {
        return value instanceof HttpServletRequest ||
                value instanceof MultipartFile ||
                value instanceof BindingResult;
    }

}

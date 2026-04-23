package com.wzkris.common.web.aspect;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.web.annotation.ExcludeLogAspect;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求统计打印
 *
 * @author wzkris
 */
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
@Aspect
public class ControllerLogAspect {

    private static final ConcurrentHashMap<String, Boolean> excludeControllers = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper;

    public ControllerLogAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.copy();
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Pointcut("bean(*Controller)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String fullMethodName = joinPoint.getSignature().getDeclaringType().getName() + StringUtil.DOT + joinPoint.getSignature().getName();
        Boolean bool = excludeControllers.computeIfAbsent(fullMethodName, k -> {
            // 检查是否包含排除注解
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

            // 如果方法或类上有排除注解，返回 true 表示排除
            return method.isAnnotationPresent(ExcludeLogAspect.class) ||
                    method.getDeclaringClass().isAnnotationPresent(ExcludeLogAspect.class);
        });

        if (bool) {
            return joinPoint.proceed();
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes == null ? null : attributes.getRequest();
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
                    request == null ? null : request.getRequestURL(),
                    request == null ? null : request.getMethod(),
                    this.getRequestParams(request),
                    this.getRequestBody(joinPoint),
                    this.serializeValue(result),
                    endTime - startTime);
        }

        return result;
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

package com.wzkris.common.web.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * 请求统计
 *
 * @author wzkris
 */
@Slf4j
@Aspect
public class ControllerStatisticAspect {

    @Pointcut("bean(*Controller) && within(@org.springframework.web.bind.annotation.RestController *)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        long startTime = 0L;
        long endTime = 0L;
        Object result = null;

        try {
            startTime = System.currentTimeMillis();
            result = joinPoint.proceed();
            endTime = System.currentTimeMillis();
        } finally {
            log.info("""
                            Request URL: {}
                            Request Method: {}
                            Request Parameters: {}
                            Request Body: {}
                            Response: {}
                            Time Taken: {} ms
                            """,
                    request.getRequestURL(),
                    request.getMethod(),
                    this.getRequestParams(request),
                    this.getRequestBody(joinPoint),
                    result,
                    endTime - startTime);
        }

        return result;
    }

    private String getRequestParams(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        StringBuilder sb = new StringBuilder();

        for (String key : paramMap.keySet()) {
            sb.append(key).append("=").append(Arrays.toString(paramMap.get(key))).append(",");
        }

        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
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
                if (!(arg instanceof HttpServletRequest) && !(arg instanceof MultipartFile)) {
                    return Optional.ofNullable(arg).map(Object::toString).orElse("");
                }
            }
        }

        return null;
    }

}

package com.wzkris.common.web.annotation.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
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
@Component
public class RequestStatisticAspect {

    @Pointcut("bean(*Controller)")
    public void reqPointCut() {
    }

    @Around("reqPointCut()")
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
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (!(arg instanceof HttpServletRequest)
                    && !(arg instanceof RequestBody) && !(arg instanceof MultipartFile)) {
                return Optional.ofNullable(arg).map(Object::toString).orElse("");
            }
        }

        return null;
    }

}

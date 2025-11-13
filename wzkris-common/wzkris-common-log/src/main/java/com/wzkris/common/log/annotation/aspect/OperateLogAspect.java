package com.wzkris.common.log.annotation.aspect;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.event.OperateEvent;
import com.wzkris.common.security.utils.TenantUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Stream;

/**
 * 日志切面 - 支持Web和非Web环境
 *
 * @author wzkris
 */
@Slf4j
@Aspect
public class OperateLogAspect {

    private static final int MAX_PARAM_LENGTH = 1000;

    private static final int MAX_ERROR_LENGTH = 1000;

    private static final int MAX_URL_LENGTH = 150;

    /**
     * 敏感属性字段
     */
    public final String[] EXCLUDE_PROPERTIES = {
            "pwd", "passwd", "password", "oldPassword", "newPassword", "confirmPassword"
    };

    private final ObjectMapper objectMapper = JsonUtil.getObjectMapper().copy();

    public OperateLogAspect() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * 处理完请求后执行
     */
    @AfterReturning(pointcut = "@annotation(operateLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, OperateLog operateLog, Object jsonResult) {
        handleLog(joinPoint, operateLog, jsonResult, null);
    }

    /**
     * 拦截异常操作
     */
    @AfterThrowing(pointcut = "@annotation(operateLog)", throwing = "exception")
    public void doAfterThrowing(JoinPoint joinPoint, OperateLog operateLog, Exception exception) {
        handleLog(joinPoint, operateLog, null, exception);
    }

    protected void handleLog(final JoinPoint joinPoint, OperateLog operateLog, Object jsonResult, Exception exception) {
        OperateEvent operateEvent = buildOperateEvent(joinPoint, operateLog, jsonResult, exception);

        SpringUtil.getContext().publishEvent(operateEvent);
    }

    /**
     * 构建操作事件 - 不依赖HTTP请求
     */
    private OperateEvent buildOperateEvent(JoinPoint joinPoint, OperateLog operateLog,
                                           Object jsonResult, Exception exception) {
        OperateEvent operateEvent = new OperateEvent();

        // 设置用户信息
        operateEvent.setOperatorId(SecurityUtil.getId());
        operateEvent.setAuthType(SecurityUtil.getAuthType().getValue());
        operateEvent.setOperName(SecurityUtil.getName());

        // 设置租户ID
        setTenantId(operateEvent);

        // 设置操作信息
        operateEvent.setOperType(operateLog.operateType().getValue());
        operateEvent.setSuccess(true);
        operateEvent.setOperTime(new Date());

        // 设置方法信息
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        operateEvent.setMethod(className + StringUtil.DOT + methodName + "()");

        // 对于Web环境，尝试获取请求信息；非Web环境则为空
        setRequestParams(operateEvent);

        // 处理异常情况
        if (exception != null) {
            operateEvent.setSuccess(false);
            operateEvent.setErrorMsg(StringUtil.substring(exception.getMessage(), 0, MAX_ERROR_LENGTH));
        } else if (jsonResult instanceof Result<?> result && !result.isSuccess()) {
            operateEvent.setSuccess(false);
            operateEvent.setErrorMsg(StringUtil.substring(result.getMessage(), 0, MAX_ERROR_LENGTH));
        }

        // 设置注解信息
        operateEvent.setTitle(operateLog.title());
        operateEvent.setSubTitle(operateLog.subTitle());
        operateEvent.setOperType(operateLog.operateType().getValue());

        // 处理参数和结果
        try {
            setRequestValue(joinPoint, operateLog.excludeRequestParam(), operateEvent);

            if (jsonResult != null) {
                operateEvent.setJsonResult(StringUtil.substring(
                        objectMapper.writeValueAsString(jsonResult), 0, MAX_PARAM_LENGTH));
            }
        } catch (JsonProcessingException e) {
            log.error("日志参数转换发生异常：{}", e.getMessage(), e);
        }

        return operateEvent;
    }

    /**
     * 设置Web环境信息（如果存在）
     */
    private void setRequestParams(OperateEvent operateEvent) {
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes();

            HttpServletRequest request = requestAttributes.getRequest();

            String ip = ServletUtil.getClientIP(request);
            operateEvent.setRequestMethod(request.getMethod());
            operateEvent.setOperIp(ip);
            operateEvent.setOperUrl(StringUtil.substring(request.getRequestURI(), 0, MAX_URL_LENGTH));

        } catch (Exception e) {
            log.info("非Web环境，跳过设置请求信息");
        }
    }

    private void setRequestValue(JoinPoint joinPoint, String[] excludeRequestParam, OperateEvent operateEvent)
            throws JsonProcessingException {
        String operParams = argsArrayToString(joinPoint.getArgs());

        if (StringUtil.isNotBlank(operParams)) {
            // 如果是JSON格式，尝试解析并过滤敏感字段
            if (operParams.startsWith("{")) {
                Map<String, String> paramsMap = objectMapper.readValue(
                        operParams,
                        TypeFactory.defaultInstance().constructMapType(
                                HashMap.class, String.class, Object.class));
                if (!paramsMap.isEmpty()) {
                    fuzzyParams(paramsMap, excludeRequestParam);
                    operParams = StringUtil.substring(
                            objectMapper.writeValueAsString(paramsMap), 0, MAX_PARAM_LENGTH);
                }
            } else {
                operParams = StringUtil.substring(operParams, 0, MAX_PARAM_LENGTH);
            }
        }

        operateEvent.setOperParam(operParams);
    }

    private void fuzzyParams(Map<String, String> paramsMap, String[] excludeRequestParam) {
        Stream.concat(
                        Arrays.stream(EXCLUDE_PROPERTIES),
                        Arrays.stream(excludeRequestParam != null ? excludeRequestParam : new String[0]))
                .forEach(property -> {
                    if (paramsMap.containsKey(property)) {
                        paramsMap.put(property, "*");
                    }
                });
    }

    private String argsArrayToString(Object[] paramsArray) {
        if (paramsArray == null || paramsArray.length == 0) {
            return StringUtil.EMPTY;
        }

        StringBuilder params = new StringBuilder();
        for (Object o : paramsArray) {
            if (o != null && !isFilterObject(o)) {
                try {
                    String jsonObj = objectMapper.writeValueAsString(o);
                    params.append(jsonObj).append(StringUtil.SPACE);
                } catch (Exception ignored) {
                    // 序列化失败，使用toString方法
                    params.append(o).append(StringUtil.SPACE);
                }
            }
        }
        return params.toString().trim();
    }

    private boolean isFilterObject(final Object o) {
        if (o instanceof MultipartFile ||
                o instanceof BindingResult) {
            return true;
        }

        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection<?> collection = (Collection<?>) o;
            return !collection.isEmpty() && collection.iterator().next() instanceof MultipartFile;
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map<?, ?> map = (Map<?, ?>) o;
            return !map.isEmpty() && map.values().iterator().next() instanceof MultipartFile;
        }

        return false;
    }

    /**
     * 设置租户ID
     */
    private void setTenantId(OperateEvent operateEvent) {
        try {
            String authType = operateEvent.getAuthType();
            if (StringUtil.equals(authType, AuthTypeEnum.TENANT.getValue())) {
                operateEvent.setTenantId(TenantUtil.getTenantId());
            } else {
                // 对于其他认证类型，暂时设置为null
                operateEvent.setTenantId(null);
            }
        } catch (Exception e) {
            // 在非认证环境或获取租户ID失败时，设置为null
            log.debug("获取租户ID失败，设置为null: {}", e.getMessage());
            operateEvent.setTenantId(null);
        }
    }

}
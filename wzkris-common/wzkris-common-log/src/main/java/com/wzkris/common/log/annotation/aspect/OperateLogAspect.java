package com.wzkris.common.log.annotation.aspect;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.AddressUtil;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateStatus;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.system.api.RemoteLogApi;
import com.wzkris.system.api.domain.request.OperLogReq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpMethod;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志切面
 *
 * @author wzkris
 */
@Slf4j
@Aspect
public class OperateLogAspect {

    /**
     * 敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES =
            {"pwd", "passwd", "password", "oldPassword", "newPassword", "confirmPassword"};

    private final ObjectMapper objectMapper = JsonUtil.getObjectMapper().copy();

    @DubboReference
    private final RemoteLogApi remoteLogApi;

    public OperateLogAspect(RemoteLogApi remoteLogApi) {
        this.remoteLogApi = remoteLogApi;
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);// 配置null不序列化, 避免大量无用参数存入DB
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(operateLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, OperateLog operateLog, Object jsonResult) {
        handleLog(joinPoint, operateLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     */
    @AfterThrowing(pointcut = "@annotation(operateLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, OperateLog operateLog, Exception e) {
        handleLog(joinPoint, operateLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, OperateLog operateLog, final Exception e, Object jsonResult) {
        try {
            // *========数据库日志=========*//
            OperLogReq operLogReq = new OperLogReq();
            operLogReq.setUserId(LoginUserUtil.getUserId());
            operLogReq.setOperName(LoginUserUtil.getUsername());
            operLogReq.setTenantId(LoginUserUtil.getTenantId());
            operLogReq.setOperType(operateLog.operateType().getValue());
            operLogReq.setStatus(OperateStatus.SUCCESS.value());
            operLogReq.setOperTime(DateUtil.date());
            // 请求的地址
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String ip = ServletUtil.getClientIP(request);
            operLogReq.setOperIp(ip);
            operLogReq.setOperLocation(AddressUtil.getRealAddressByIp(ip));
            operLogReq.setOperUrl(StringUtil.sub(request.getRequestURI(), 0, 255));
            if (e != null) {
                operLogReq.setStatus(OperateStatus.FAIL.value());
                operLogReq.setErrorMsg(StringUtil.sub(e.getMessage(), 0, 2000));
            } else if (jsonResult instanceof Result<?> result) {
                if (!result.isSuccess()) {
                    operLogReq.setStatus(OperateStatus.FAIL.value());
                    operLogReq.setErrorMsg(StringUtil.sub(result.getMessage(), 0, 2000));
                }
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLogReq.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            operLogReq.setRequestMethod(request.getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, operateLog, operLogReq, jsonResult);
            // 保存数据库
            remoteLogApi.insertOperlog(operLogReq);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("日志切面发生异常，异常信息:{}", exp.getMessage(), exp);
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log        日志
     * @param operLogReq 操作日志
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, OperateLog log, OperLogReq operLogReq,
                                               Object jsonResult) throws JsonProcessingException {
        // 设置action动作
        operLogReq.setOperType(log.operateType().getValue());
        // 设置标题
        operLogReq.setTitle(log.title());
        operLogReq.setSubTitle(log.subTitle());
        // 设置操作人类别
        operLogReq.setOperatorType(log.operateType().getValue());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, log.excludeRequestParam(), operLogReq);
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && StringUtil.isNotNull(jsonResult)) {
            operLogReq.setJsonResult(StringUtil.sub(objectMapper.writeValueAsString(jsonResult), 0, 2000));
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLogReq 操作日志
     */
    private void setRequestValue(JoinPoint joinPoint, String[] excludeRequestParam, OperLogReq operLogReq) throws JsonProcessingException {
        Map<String, String> paramsMap = new HashMap<>();
        String operParams = "";
        final String requestMethod = operLogReq.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = this.argsArrayToString(joinPoint.getArgs());
            if (StringUtil.isNotBlank(params) && params.startsWith("{")) {
                paramsMap = objectMapper.readValue(params, TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Object.class));
            } else {
                operParams = params;
            }
        } else {
            paramsMap = ServletUtil.getParamMap(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        }

        if (!paramsMap.isEmpty()) {
            this.fuzzyParams(paramsMap, excludeRequestParam);// 移除敏感字段
            operParams = StringUtil.sub(objectMapper.writeValueAsString(paramsMap), 0, 2000);
        }
        operLogReq.setOperParam(operParams);
    }

    /**
     * 敏感字段模糊处理
     */
    private void fuzzyParams(Map<String, String> paramsMap, String[] excludeRequestParam) {
        for (String property : OperateLogAspect.EXCLUDE_PROPERTIES) {
            String value = paramsMap.get(property);
            if (value != null) {
                paramsMap.put(property, "**");
            }
        }
        for (String property : excludeRequestParam) {
            String value = paramsMap.get(property);
            if (value != null) {
                paramsMap.put(property, "**");
            }
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null) {
            for (Object o : paramsArray) {
                if (StringUtil.isNotNull(o) && !isFilterObject(o)) {
                    try {
                        String jsonObj = objectMapper.writeValueAsString(o);
                        params.append(jsonObj).append(" ");
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}

package com.thingslink.common.log.aspect;

import cn.hutool.core.map.MapUtil;
import com.thingslink.common.core.utils.ServletUtil;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.BusinessStatus;
import com.thingslink.common.log.enums.OperatorType;
import com.thingslink.common.security.utils.LoginUserUtil;
import com.thingslink.system.api.RemoteLogApi;
import com.thingslink.system.api.domain.OperLogDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Map;

/**
 * 日志切面
 *
 * @author wzkris
 */
@Slf4j
@Aspect
@Component
public class OperateLogAspect {

    /**
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword"};

    @Autowired
    private RemoteLogApi remoteLogApi;

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(operateLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, OperateLog operateLog, Object jsonResult) {
        handleLog(joinPoint, operateLog, null, jsonResult);
    }

    protected void handleLog(final JoinPoint joinPoint, OperateLog operateLog, final Exception e, Object jsonResult) {
        try {
            // *========数据库日志=========*//
            OperLogDTO operLogDTO = new OperLogDTO();
            operLogDTO.setStatus(BusinessStatus.SUCCESS.value());
            // 请求的地址
            String ip = ServletUtil.getClientIP(ServletUtil.getRequest());
            operLogDTO.setOperIp(ip);
            operLogDTO.setOperUrl(StringUtil.sub(ServletUtil.getRequest().getRequestURI(), 0, 255));
            String username = "";
            if (operateLog.operatorType() == OperatorType.USER && LoginUserUtil.isLogin()) {
                username = LoginUserUtil.getLoginUser().getUsername();
            }
            else if (operateLog.operatorType() == OperatorType.CUSTOMER && LoginUserUtil.isLogin()) {
                username = LoginUserUtil.getLoginUser().getUsername();
            }
            if (StringUtil.isNotBlank(username)) {
                operLogDTO.setOperName(username);
            }
            if (e != null) {
                operLogDTO.setStatus(BusinessStatus.FAIL.value());
                operLogDTO.setErrorMsg(StringUtil.sub(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLogDTO.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            operLogDTO.setRequestMethod(ServletUtil.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, operateLog, operLogDTO, jsonResult);
            // 保存数据库
            remoteLogApi.insertOperlog(operLogDTO);
        }
        catch (Exception exp) {
            // 记录本地异常日志
            log.error("日志切面发生异常，异常信息:{}", exp.getMessage(), exp);
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log        日志
     * @param operLogDTO 操作日志
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, OperateLog log, OperLogDTO operLogDTO,
                                               Object jsonResult) {
        // 设置action动作
        operLogDTO.setBusinessType(log.businessType().ordinal());
        // 设置标题
        operLogDTO.setTitle(log.title());
        // 设置操作人类别
        operLogDTO.setOperatorType(log.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operLogDTO);
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && StringUtil.isNotNull(jsonResult)) {
            operLogDTO.setJsonResult(StringUtil.sub(JsonUtil.toJsonString(jsonResult), 0, 2000));
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLogDTO 操作日志
     */
    private void setRequestValue(JoinPoint joinPoint, OperLogDTO operLogDTO) {
        String requestMethod = operLogDTO.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            operLogDTO.setOperParam(StringUtil.sub(params, 0, 2000));
        }
        else {
            Map<String, String> paramsMap = ServletUtil.getParamMap(ServletUtil.getRequest());
            MapUtil.removeAny(paramsMap, EXCLUDE_PROPERTIES);
            operLogDTO.setOperParam(StringUtil.sub(JsonUtil.toJsonString(paramsMap), 0, 2000));
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
                        String jsonObj = JsonUtil.toJsonString(o);
                        params.append(jsonObj).append(" ");
                    }
                    catch (Exception ignored) {
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
        }
        else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        }
        else if (Map.class.isAssignableFrom(clazz)) {
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
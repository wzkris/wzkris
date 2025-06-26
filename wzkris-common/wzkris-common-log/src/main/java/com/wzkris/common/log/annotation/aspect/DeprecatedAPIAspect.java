package com.wzkris.common.log.annotation.aspect;

import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.service.GenericException;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.DeprecatedAPI;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 废弃API切面
 *
 * @author wzkris
 */
@Slf4j
@Aspect
public class DeprecatedAPIAspect {

    private static final String VERSION = "version";

    /**
     * 若是API则在调用前返回，方法则执行
     *
     * @param joinPoint 切点
     */
    @Before(value = "@annotation(deprecatedAPI)")
    public void doBefore(JoinPoint joinPoint, DeprecatedAPI deprecatedAPI) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(signature.getMethod(), RequestMapping.class);

        if (requestMapping == null) {
            log.error(
                    "废弃方法被调用：{}",
                    signature.getDeclaringTypeName()
                            + StringUtil.AT
                            + signature.getMethod().getName());
            return;
        }

        if (StringUtil.isBlank(deprecatedAPI.requiredVersion())) {
            throw new GenericException(BizCode.DEPRECATED_API.value(), "不支持此API版本");
        } else {
            if (StringUtil.equals(deprecatedAPI.requiredVersion(), getRequestVersion())) {// 版本号一致则执行
                return;
            }
            throw new GenericException(BizCode.DEPRECATED_API.value(), "不支持此API版本");
        }
    }

    @Nullable
    private String getRequestVersion() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String version = request.getHeader(VERSION);
        return version == null ? request.getParameter(VERSION) : version;
    }

}

package com.wzkris.common.openfeign.annotation.aspect;

import com.wzkris.common.core.exception.BusinessExceptionI18n;
import com.wzkris.common.openfeign.annotation.InnerAuth;
import com.wzkris.common.openfeign.config.IdentityProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 内部请求切面校验
 * @date : 2024/7/3 13:25
 */
@Aspect
@AllArgsConstructor
public class InnerAuthAspect implements Ordered {

    private final IdentityProperties identityProperties;

    /**
     * 拦截标记方法或标记类下的所有方法
     * 注意：若类和方法同时标记了，则会切入类上的注解，若欲扩展特殊权限校验则必须手动获取方法上的注解
     */
    @Before("@annotation(innerAuth) || @within(innerAuth)")
    public void before(JoinPoint joinPoint, InnerAuth innerAuth) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return;
        }

        if (identityProperties.getIdentityKey() != null && identityProperties.getIdentityValue() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String requestIdentityValue = request.getHeader(identityProperties.getIdentityKey());
            if (requestIdentityValue == null || !requestIdentityValue.equals(identityProperties.getIdentityValue())) {
                throw new BusinessExceptionI18n(403, "request.inner.forbid");
            }
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}

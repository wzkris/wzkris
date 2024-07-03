package com.thingslink.common.security.annotation.aspect;

import com.thingslink.common.core.exception.BusinessExceptionI18n;
import com.thingslink.common.core.utils.ServletUtil;
import com.thingslink.common.security.annotation.InnerAuth;
import com.thingslink.common.security.oauth2.config.OAuth2Properties;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 内部请求切面校验
 * @date : 2024/7/3 13:25
 */
@Aspect
@Component
@AllArgsConstructor
public class InnerAuthAspect implements Ordered {

    private final OAuth2Properties oAuth2Properties;

    /**
     * 拦截标记方法或标记类下的所有方法
     * 注意：若类和方法同时标记了，则会切入类上的注解，若欲扩展特殊权限校验则必须手动获取方法上的注解
     */
    @Before("@annotation(innerAuth) || @within(innerAuth)")
    public void before(JoinPoint joinPoint, InnerAuth innerAuth) {
        if (oAuth2Properties.getIdentityKey() != null && oAuth2Properties.getIdentityValue() != null) {
            String identityValue = oAuth2Properties.getIdentityValue();
            String requestIdentityValue = ServletUtil.getHeader(oAuth2Properties.getIdentityKey());
            if (requestIdentityValue == null || !requestIdentityValue.equals(identityValue)) {
                throw new BusinessExceptionI18n(403, "request.inner.forbid");
            }
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}

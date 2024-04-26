package com.thingslink.common.security.annotation.aspect;

import com.thingslink.common.core.constant.SecurityConstants;
import com.thingslink.common.core.utils.MessageUtil;
import com.thingslink.common.core.utils.ServletUtil;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.security.annotation.InnerAuth;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 内部请求切面校验
 * @date : 2023/8/4 11:08
 */
@Aspect
@Component
public class InnerAspect implements Ordered {
    // 当前服务名
    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 拦截标记方法或标记类下的所有方法
     * 注意：若类和方法同时标记了，则会切入类上的注解，若欲扩展特殊权限校验则必须手动获取方法上的注解
     */
    @Before("@annotation(innerAuth) || @within(innerAuth)")
    public void before(JoinPoint joinPoint, InnerAuth innerAuth) {
        // 校验内部权限
        String target = ServletUtil.getHeader(SecurityConstants.INNER_REQUEST_HEADER);
        // 内部请求验证
        if (!StringUtil.equalsIgnoreCase(applicationName, target)) {
            throw new AccessDeniedException(MessageUtil.message("request.inner.forbid"));
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}

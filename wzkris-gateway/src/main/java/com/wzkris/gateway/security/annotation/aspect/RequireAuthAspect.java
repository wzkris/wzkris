package com.wzkris.gateway.security.annotation.aspect;

import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.openfeign.exception.RpcException;
import com.wzkris.gateway.filter.web.UnifiedAuthenticationFilter;
import com.wzkris.gateway.security.annotation.RequireAuth;
import com.wzkris.gateway.security.checker.AuthChecker;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.reactivestreams.Publisher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * 权限验证切面
 * <p>用于WebFlux环境下的权限验证
 *
 * @author wzkris
 */
@Slf4j
@Aspect
@Order(1)
@Component
public class RequireAuthAspect {

    @Pointcut("@annotation(com.wzkris.gateway.security.annotation.RequireAuth)")
    public void pointCutMethod() {
    }

    @Pointcut("@within(com.wzkris.gateway.security.annotation.RequireAuth)")
    public void pointCutClass() {
    }

    /**
     * 类级别的权限验证
     */
    @Around("pointCutClass()")
    public Object beforeClass(ProceedingJoinPoint point) {
        RequireAuth requireAuth = point.getTarget().getClass().getAnnotation(RequireAuth.class);
        return validatePermission(point, requireAuth);
    }

    /**
     * 方法级别的权限验证（优先级高于类级别）
     */
    @Around("pointCutMethod()")
    public Object beforeMethod(ProceedingJoinPoint point) {
        RequireAuth requireAuth = ((MethodSignature) point.getSignature())
                .getMethod()
                .getAnnotation(RequireAuth.class);
        return validatePermission(point, requireAuth);
    }

    /**
     * 验证权限
     */
    private Publisher<Object> validatePermission(ProceedingJoinPoint point, RequireAuth requireAuth) {
        return Mono.deferContextual(contextView -> {
            MyPrincipal principal = UnifiedAuthenticationFilter.getPrincipal(contextView)
                    .orElse(null);

            if (principal == null) {
                return Mono.error(new RpcException(401, BizBaseCode.UNAUTHORIZED.value(), BizBaseCode.UNAUTHORIZED.desc()));
            }

            boolean passed = AuthChecker.check(principal, requireAuth);
            if (!passed) {
                String authType = requireAuth.authType().getValue();
                String[] permissions = requireAuth.permissions();

                log.error("'{}'权限验证失败 - 方法: {}, 要求类型: {}, 要求权限: {}",
                        principal.getName(),
                        point.getTarget().getClass().getName() + StringUtil.DOT + ((MethodSignature) point.getSignature()).getMethod().getName(),
                        authType,
                        Arrays.toString(permissions));

                return Mono.error(new RpcException(403, BizBaseCode.FORBID.value(), BizBaseCode.FORBID.desc()));
            }

            try {
                Object result = point.proceed();

                if (result instanceof Mono) {
                    return ((Mono<?>) result).cast(Object.class);
                }
                if (result instanceof Flux) {
                    return ((Flux<?>) result).collectList().cast(Object.class);
                }
                if (result instanceof Publisher) {
                    return Mono.from((Publisher<?>) result).cast(Object.class);
                }
                return Mono.justOrEmpty(result);
            } catch (Throwable e) {
                log.error("切面发生异常: ", e);
                return Mono.error(e);
            }
        });
    }

}


package com.wzkris.gateway.security.annotation.aspect;

import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.exception.service.ResultException;
import com.wzkris.common.core.model.LoginUser;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.gateway.security.annotation.RequireAuth;
import com.wzkris.gateway.security.checker.AuthChecker;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Set;

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
    private Mono<Object> validatePermission(ProceedingJoinPoint point, RequireAuth requireAuth) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .switchIfEmpty(Mono.error(new ResultException(401, BizBaseCodeEnum.AUTHENTICATION_ERROR.value(), BizBaseCodeEnum.AUTHENTICATION_ERROR.desc())))
                .flatMap(authentication -> {
                    Object principalObj = authentication.getPrincipal();
                    if (!(principalObj instanceof LoginUser loginUser)) {
                        return Mono.error(new ResultException(401, BizBaseCodeEnum.AUTHENTICATION_ERROR.value(), BizBaseCodeEnum.AUTHENTICATION_ERROR.desc()));
                    }

                    // 从 Authentication 获取权限
                    Set<String> permissions = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

                    boolean passed = AuthChecker.check(loginUser, permissions, requireAuth);
                    if (!passed) {
                        String authType = requireAuth.authType().getValue();
                        String[] requirePermissions = requireAuth.permissions();

                        log.error("'{}'权限验证失败 - 方法: {}, 要求类型: {}, 要求权限: {}",
                                loginUser.getName(),
                                point.getTarget().getClass().getName() + StringUtil.DOT + ((MethodSignature) point.getSignature()).getMethod().getName(),
                                authType,
                                Arrays.toString(requirePermissions));

                        return Mono.error(new ResultException(403, BizBaseCodeEnum.ACCESS_DENIED.value(), BizBaseCodeEnum.ACCESS_DENIED.desc()));
                    }

                    try {
                        Object result = point.proceed();

                        if (result instanceof Mono<?> mono) {
                            return mono.cast(Object.class);
                        }
                        if (result instanceof Flux<?> flux) {
                            return flux.collectList().cast(Object.class);
                        }
                        // 处理其他实现了 Publisher 接口的类型（非 Mono/Flux）
                        if (result instanceof org.reactivestreams.Publisher<?> publisher) {
                            return Mono.from(publisher).cast(Object.class);
                        }
                        return Mono.justOrEmpty(result);
                    } catch (Throwable e) {
                        log.error("切面发生异常: ", e);
                        return Mono.error(e);
                    }
                });
    }

}


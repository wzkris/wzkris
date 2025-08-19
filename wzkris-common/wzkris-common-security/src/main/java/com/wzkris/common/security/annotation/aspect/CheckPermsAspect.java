package com.wzkris.common.security.annotation.aspect;

import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.common.security.annotation.enums.CheckMode;
import com.wzkris.common.security.oauth2.service.PermissionService;
import com.wzkris.common.security.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

/**
 * 权限切面
 *
 * @author wzkris
 */
@Slf4j
@Aspect
@Order(-1)
@Component
public class CheckPermsAspect {

    @Autowired
    private PermissionService permissionService;

    @Pointcut("@annotation(com.wzkris.common.security.annotation.CheckPerms)"
            + "|| @annotation(com.wzkris.common.security.annotation.CheckSystemPerms)"
            + "|| @within(com.wzkris.common.security.annotation.CheckPerms)"
            + "|| @within(com.wzkris.common.security.annotation.CheckSystemPerms)")
    public void pointcut() {
    }

    /**
     * 方法执行前执行
     */
    @Before("pointcut()")
    public void before(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        CheckPerms checkPerms = AnnotatedElementUtils.findMergedAnnotation(signature.getMethod(), CheckPerms.class);

        // 如果方法上没有注解，尝试从类上查找
        if (checkPerms == null) {
            Class<?> targetClass = point.getTarget().getClass();
            checkPerms = AnnotatedElementUtils.findMergedAnnotation(targetClass, CheckPerms.class);
        }

        CorePrincipal principal = SecurityUtil.getPrincipal();

        if (!StringUtil.equals(principal.getType(), checkPerms.checkType().getValue())) {
            throw new AccessDeniedException(
                    "Principal needs checkType :" + checkPerms.checkType().getValue() + " , but have :" + principal.getType());
        }
        String[] perms = checkPerms.value();

        if (ArrayUtils.isEmpty(perms)) {
            return;
        }

        CheckMode mode = checkPerms.mode();
        Set<String> grantedAuthority = principal.getPermissions();

        if (mode == CheckMode.AND) {
            if (!permissionService.hasPerms(grantedAuthority, perms)) {
                throw new AccessDeniedException("AuthUser: '" + principal.getName() + "' missing permission : "
                        + Arrays.toString(perms) + " one of them");
            }
        }

        if (mode == CheckMode.OR) {
            if (!permissionService.hasPermsOr(grantedAuthority, perms)) {
                throw new AccessDeniedException(
                        "AuthUser: '" + principal.getName() + "' missing all permission: " + Arrays.toString(perms));
            }
        }
    }

}

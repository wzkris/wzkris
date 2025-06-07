package com.wzkris.common.security.oauth2.annotation.aspect;

import cn.hutool.core.util.ObjUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.oauth2.annotation.CheckPerms;
import com.wzkris.common.security.oauth2.domain.AuthBaseUser;
import com.wzkris.common.security.oauth2.service.PermissionService;
import com.wzkris.common.security.utils.SecurityUtil;
import java.util.Arrays;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;

/**
 * 权限切面
 *
 * @author wzkris
 */
@Slf4j
@Aspect
@Order(-1)
public class CheckPermsAspect {

    @Autowired
    private PermissionService permissionService;

    @Pointcut("@annotation(com.wzkris.common.security.oauth2.annotation.CheckPerms)"
            + "|| @annotation(com.wzkris.common.security.oauth2.annotation.CheckSystemPerms)"
            + "|| @within(com.wzkris.common.security.oauth2.annotation.CheckPerms)"
            + "|| @within(com.wzkris.common.security.oauth2.annotation.CheckSystemPerms)")
    public void pointcut() {}

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

        AuthBaseUser baseUser = SecurityUtil.getPrincipal();

        if (ObjUtil.notEqual(baseUser.getLoginType(), checkPerms.checkType())) {
            throw new AccessDeniedException(
                    "AuthUser needs checkType :" + checkPerms.checkType() + " , but have " + baseUser.getLoginType());
        }
        String[] perms = checkPerms.value();

        if (StringUtil.isEmpty(perms)) {
            return;
        }

        CheckPerms.Mode mode = checkPerms.mode();
        Set<String> grantedAuthority = baseUser.getGrantedAuthority();

        if (mode == CheckPerms.Mode.AND) {
            if (!permissionService.hasPerms(grantedAuthority, perms)) {
                throw new AccessDeniedException("AuthUser: '" + baseUser.getName() + "' missing permission : "
                        + Arrays.toString(perms) + " one of them");
            }
        }

        if (mode == CheckPerms.Mode.OR) {
            if (!permissionService.hasPermsOr(grantedAuthority, perms)) {
                throw new AccessDeniedException(
                        "AuthUser: '" + baseUser.getName() + "' missing all permission: " + Arrays.toString(perms));
            }
        }
    }
}

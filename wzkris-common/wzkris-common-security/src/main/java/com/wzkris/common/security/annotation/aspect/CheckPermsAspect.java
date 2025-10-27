package com.wzkris.common.security.annotation.aspect;

import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.common.security.annotation.enums.CheckMode;
import com.wzkris.common.security.utils.PermissionUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;

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
public class CheckPermsAspect {

    @Pointcut("@annotation(com.wzkris.common.security.annotation.CheckPerms)"
            + "|| @annotation(com.wzkris.common.security.annotation.CheckUserPerms)"
            + "|| @annotation(com.wzkris.common.security.annotation.CheckClientPerms)"
            + "|| @annotation(com.wzkris.common.security.annotation.CheckStaffPerms)"
            + "|| @within(com.wzkris.common.security.annotation.CheckPerms)"
            + "|| @within(com.wzkris.common.security.annotation.CheckUserPerms)"
            + "|| @within(com.wzkris.common.security.annotation.CheckClientPerms)"
            + "|| @within(com.wzkris.common.security.annotation.CheckStaffPerms)")
    public void pointcut() {
    }

    /**
     * 方法执行前执行
     */
    @Before("pointcut()")
    public void before(JoinPoint point) {
        CheckPerms checkPerms = findCheckPermsAnnotation(point);

        validatePermission(checkPerms);
    }

    /**
     * 查找权限注解（支持多种注解类型）
     */
    private CheckPerms findCheckPermsAnnotation(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();

        return AnnotatedElementUtils.findMergedAnnotation(signature.getMethod(), CheckPerms.class);
    }

    /**
     * 验证权限
     */
    private void validatePermission(CheckPerms checkPerms) {
        MyPrincipal principal = SecurityUtil.getPrincipal();
        validatePrincipalType(principal, checkPerms);

        String[] fullPerms = buildFullPermissions(checkPerms);
        if (ArrayUtils.isEmpty(fullPerms)) {
            return;
        }

        boolean hasPermission = checkPermission(principal.getPermissions(), fullPerms, checkPerms.mode());
        if (!hasPermission) {
            throw createAccessDeniedException(principal, fullPerms, checkPerms.mode());
        }
    }

    /**
     * 验证主体类型
     */
    private void validatePrincipalType(MyPrincipal principal, CheckPerms checkPerms) {
        if (principal == null) {
            throw new AccessDeniedException("未找到认证信息，请先登录");
        }

        String expectedType = checkPerms.checkType().getValue();
        AuthType actualType = principal.getType();

        if (!StringUtil.equals(actualType.getValue(), expectedType)) {
            throw new AccessDeniedException(
                    String.format("认证类型不匹配: 需要[%s]，实际[%s]", expectedType, actualType.getValue()));
        }
    }

    /**
     * 构建完整的权限标识（添加前缀）
     */
    private String[] buildFullPermissions(CheckPerms checkPerms) {
        if (ArrayUtils.isEmpty(checkPerms.value())) {
            return new String[0];
        }

        String prefix = checkPerms.prefix();
        return Arrays.stream(checkPerms.value())
                .map(perm -> prefix + perm)
                .toArray(String[]::new);
    }

    /**
     * 检查权限
     */
    private boolean checkPermission(Set<String> grantedAuthority, String[] requiredPerms, CheckMode mode) {
        if (mode == CheckMode.AND) {
            return PermissionUtil.hasPerms(grantedAuthority, requiredPerms);
        } else if (mode == CheckMode.OR) {
            return PermissionUtil.hasPermsOr(grantedAuthority, requiredPerms);
        }
        return false;
    }

    /**
     * 创建权限拒绝异常
     */
    private AccessDeniedException createAccessDeniedException(MyPrincipal principal, String[] perms, CheckMode mode) {
        String name = principal.getName();
        AuthType type = principal.getType();

        if (mode == CheckMode.AND) {
            return new AccessDeniedException(
                    String.format("[%s][%s]缺少以下权限之一: %s", type.getValue(), name, Arrays.toString(perms)));
        } else {
            return new AccessDeniedException(
                    String.format("[%s][%s]缺少所有权限: %s", type.getValue(), name, Arrays.toString(perms)));
        }
    }

}

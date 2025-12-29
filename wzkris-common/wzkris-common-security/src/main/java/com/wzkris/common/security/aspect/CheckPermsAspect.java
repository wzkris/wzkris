package com.wzkris.common.security.aspect;

import com.wzkris.common.core.model.UserPrincipal;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.common.security.enums.CheckMode;
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
            + "|| @annotation(com.wzkris.common.security.annotation.CheckAdminPerms)"
            + "|| @annotation(com.wzkris.common.security.annotation.CheckClientPerms)"
            + "|| @annotation(com.wzkris.common.security.annotation.CheckTenantPerms)")
    public void pointCutMethod() {
    }

    @Pointcut("@within(com.wzkris.common.security.annotation.CheckPerms)"
            + "|| @within(com.wzkris.common.security.annotation.CheckAdminPerms)"
            + "|| @within(com.wzkris.common.security.annotation.CheckClientPerms)"
            + "|| @within(com.wzkris.common.security.annotation.CheckTenantPerms)")
    public void pointCutClass() {
    }

    @Before("pointCutClass()")
    public void beforeClass(JoinPoint point) {
        CheckPerms checkPerms = AnnotatedElementUtils.findMergedAnnotation(point.getTarget().getClass(), CheckPerms.class);

        validatePermission(checkPerms);
    }

    /**
     * 方法执行前执行
     */
    @Before("pointCutMethod()")
    public void beforeMethod(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();

        CheckPerms checkPerms = AnnotatedElementUtils.findMergedAnnotation(signature.getMethod(), CheckPerms.class);

        validatePermission(checkPerms);
    }

    /**
     * 验证权限
     */
    private void validatePermission(CheckPerms checkPerms) {
        UserPrincipal principal = SecurityUtil.getPrincipal();
        validatePrincipalType(principal, checkPerms);

        String[] fullPerms = buildFullPermissions(checkPerms);
        if (ArrayUtils.isEmpty(fullPerms)) {
            return;
        }

        boolean hasPermission = checkPermission(principal.getPerms(), fullPerms, checkPerms.mode());
        if (!hasPermission) {
            throw createAccessDeniedException(principal, fullPerms, checkPerms.mode());
        }
    }

    /**
     * 验证主体类型
     */
    private void validatePrincipalType(UserPrincipal principal, CheckPerms checkPerms) {
        if (principal == null) {
            throw new AccessDeniedException("未找到认证信息，请先登录");
        }

        String expectedType = checkPerms.checkType().getValue();
        String actualType = principal.getType();

        if (!StringUtil.equals(actualType, expectedType)) {
            throw new AccessDeniedException(
                    String.format("认证类型不匹配: 需要[%s]，实际[%s]", expectedType, actualType));
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
    private AccessDeniedException createAccessDeniedException(UserPrincipal principal, String[] perms, CheckMode mode) {
        String name = principal.getName();
        String type = principal.getType();

        if (mode == CheckMode.AND) {
            return new AccessDeniedException(
                    String.format("[%s][%s]缺少以下权限之一: %s", type, name, Arrays.toString(perms)));
        } else {
            return new AccessDeniedException(
                    String.format("[%s][%s]缺少所有权限: %s", type, name, Arrays.toString(perms)));
        }
    }

}

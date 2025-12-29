package com.wzkris.gateway.security.checker;

import com.wzkris.common.core.model.UserPrincipal;
import com.wzkris.gateway.security.annotation.RequireAuth;
import org.springframework.util.PatternMatchUtils;

import java.util.Arrays;
import java.util.Set;

/**
 * 权限检查器
 * <p>用于WebFlux环境下的权限验证
 *
 * @author wzkris
 */
public class AuthChecker {

    /**
     * 检查权限
     *
     * @param principal   对象
     * @param requireAuth 权限注解
     * @return 是否有权限
     */
    public static boolean check(UserPrincipal principal, RequireAuth requireAuth) {
        if (principal == null) {
            return false;
        }

        // 2. 验证用户类型
        if (!principal.getType().equals(requireAuth.authType().getValue())) {
            return false;
        }

        // 3. 如果没有配置权限，只验证登录即可
        if (requireAuth.permissions().length == 0) {
            return true;
        }

        // 4. 验证权限
        Set<String> grantedPermissions = principal.getPerms();
        if (grantedPermissions == null || grantedPermissions.isEmpty()) {
            return false;
        }

        return checkPermissions(grantedPermissions, requireAuth.permissions(), requireAuth.mode());
    }

    /**
     * 检查权限集合
     */
    private static boolean checkPermissions(Set<String> grantedPermissions,
                                            String[] requiredPermissions,
                                            RequireAuth.CheckMode mode) {
        if (mode == RequireAuth.CheckMode.AND) {
            // AND模式：必须拥有所有权限
            return Arrays.stream(requiredPermissions)
                    .allMatch(req -> hasAnyMatch(grantedPermissions, req));
        } else if (mode == RequireAuth.CheckMode.OR) {
            // OR模式：拥有任一权限即可
            return Arrays.stream(requiredPermissions)
                    .anyMatch(req -> hasAnyMatch(grantedPermissions, req));
        }
        return false;
    }

    private static boolean hasAnyMatch(Set<String> grantedPermissions, String requiredPermission) {
        if (grantedPermissions.contains(requiredPermission)) {
            return true;
        }
        for (String pattern : grantedPermissions) {
            if (PatternMatchUtils.simpleMatch(pattern, requiredPermission)) {
                return true;
            }
        }
        return false;
    }

}


package com.wzkris.common.security.utils;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.support.LoginUser;
import com.wzkris.common.core.model.RoleContext;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.authentication.RoleContextAuthenticationToken;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 安全工具
 * @create : 2024/04/22 12:22
 * @update : 2024/12/20 16:35
 */
public final class SecurityUtil {

    private static final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    @Nullable
    public static Authentication getAuthentication() {
        return securityContextHolderStrategy.getContext().getAuthentication();
    }

    public static void setAuthentication(Authentication authentication) {
        securityContextHolderStrategy.getContext().setAuthentication(authentication);
    }

    /**
     * 获取当前登录用户信息,未登录返回null
     *
     * @return 当前用户（实际运行时类型可能为具体 BaseUser 子类），未登录时为 null
     */
    @Nullable
    public static LoginUser getLoginUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        return principal instanceof LoginUser loginUser ? loginUser : null;
    }

    /**
     * 获取当前登录用户的角色上下文,未登录返回null
     *
     * @return 角色上下文，未登录时为 null
     */
    @Nullable
    public static RoleContext getRoleContext() {
        Authentication authentication = getAuthentication();
        if (authentication instanceof RoleContextAuthenticationToken token) {
            return token.getRoleContext();
        }
        return null;
    }

    /**
     * 获取当前登录用户权限,未登录返回空集合
     *
     * @return 当前用户权限
     */
    public static Set<String> getPermission() {
        RoleContext roleContext = getRoleContext();
        if (roleContext == null) {
            return Collections.emptySet();
        }
        List<String> authorities = roleContext.getGrantedAuthority();
        return authorities != null ? new LinkedHashSet<>(authorities) : Collections.emptySet();
    }

    /**
     * 判断是否管理员用户
     *
     * @return 管理员用户
     */
    public static boolean isSuperUser() {
        RoleContext roleContext = getRoleContext();
        if (roleContext == null) {
            return false;
        }
        return roleContext.isSuperUser();
    }

    /**
     * 是否认证
     */
    public static boolean isLogin() {
        Authentication authentication = getAuthentication();
        return authentication instanceof RoleContextAuthenticationToken && authentication.isAuthenticated();
    }

    /**
     * 是否对应认证类型
     */
    public static boolean checkAuthType(AuthTypeEnum authTypeEnum) {
        Authentication authentication = getAuthentication();
        if (!(authentication instanceof RoleContextAuthenticationToken rca && authentication.isAuthenticated())) {
            return false;
        }
        return ((LoginUser) rca.getPrincipal()).getAuthType() == authTypeEnum;
    }

    /**
     * 获取请求的token
     */
    @Nullable
    public static String getTokenValue() {
        try {
            Authentication authentication = getAuthentication();
            return authentication == null ? null : authentication.getCredentials().toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前ID,未登录返回null
     *
     * @return 登录ID，未登录时为 null
     */
    @Nullable
    public static Long getUid() {
        LoginUser loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUid();
    }

    /**
     * 获取当前认证类型,未登录返回null
     *
     * @return 登录类型，未登录时为 null
     */
    @Nullable
    public static AuthTypeEnum getAuthType() {
        LoginUser loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getAuthType();
    }

    /**
     * 获取租户ID,未登录返回null
     *
     * @return 租户ID，未登录时为 null
     */
    @Nullable
    public static Long getTenantId() {
        LoginUser loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getTenantId();
    }

    /**
     * 获取当前标签,未登录返回空字符串
     *
     * @return 标签
     */
    public static String getHint() {
        LoginUser loginUser = getLoginUser();
        return loginUser == null ? StringUtil.EMPTY : StringUtil.defaultIfEmpty(loginUser.getHint(), StringUtil.EMPTY);
    }

}

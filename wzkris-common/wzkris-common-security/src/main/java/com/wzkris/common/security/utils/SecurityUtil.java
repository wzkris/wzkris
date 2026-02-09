package com.wzkris.common.security.utils;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.enums.IdentityTypeEnum;
import com.wzkris.common.core.exception.token.TokenExpiredException;
import com.wzkris.common.core.model.LoginUser;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 安全工具
 * @create : 2024/04/22 12:22
 * @update : 2024/12/20 16:35
 */
@Component("su")
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
     * 获取当前登录用户信息,未登录抛出异常
     *
     * @return 当前用户
     */
    public static LoginUser getLoginUser() {
        try {
            Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
            return (LoginUser) authentication.getPrincipal();
        } catch (Exception e) {
            throw new TokenExpiredException(401, "forbidden.accessDenied.tokenExpired");
        }
    }

    /**
     * 获取当前登录用户权限,未登录抛出异常
     *
     * @return 当前用户
     */
    public static Set<String> getPermission() {
        try {
            Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
            return AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        } catch (Exception e) {
            throw new TokenExpiredException(401, "forbidden.accessDenied.tokenExpired");
        }
    }

    /**
     * 是否认证
     */
    public static boolean isAuth() {
        Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
        return authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof LoginUser;
    }

    /**
     * 是否对应认证类型
     */
    public static boolean isAuth(AuthTypeEnum authTypeEnum) {
        Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
        return authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof LoginUser
                && ((LoginUser) authentication.getPrincipal()).getAuthType() == authTypeEnum;
    }

    /**
     * 获取请求的token
     */
    public static String getTokenValue() {
        try {
            Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
            return authentication == null ? null : authentication.getCredentials().toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前ID
     *
     * @return 登录ID
     */
    public static Long getUid() {
        return getLoginUser().getUid();
    }

    /**
     * 获取当前认证类型,未登录抛出异常
     *
     * @return 登录类型
     */
    public static AuthTypeEnum getAuthType() {
        return getLoginUser().getAuthType();
    }

    @Nullable
    public static String getPhoneNumber() {
        return getLoginUser().getPhoneNumber();
    }

    @Nullable
    public static String getUsername() {
        return getLoginUser().getUsername();
    }

    @Nullable
    public static Long getTenantId() {
        return getLoginUser().getTenantId();
    }

    /**
     * 获取当前标签
     *
     * @return 标签
     */
    public static String getHint() {
        return getLoginUser().getHint();
    }

    /**
     * 获取当前身份类型
     *
     * @return 身份类型
     */
    @Nullable
    public static IdentityTypeEnum getIdentityType() {
        return getLoginUser().getIdentityType();
    }

    /**
     * 是否超级管理员
     */
    public static boolean isSuper() {
        IdentityTypeEnum identityType = getIdentityType();
        if (isAuth(AuthTypeEnum.ADMIN)) {
            return identityType == IdentityTypeEnum.SUPER;
        } else if (isAuth(AuthTypeEnum.TENANT)) {
            return identityType == IdentityTypeEnum.SUPER;
        } else {
            return false;
        }
    }

}

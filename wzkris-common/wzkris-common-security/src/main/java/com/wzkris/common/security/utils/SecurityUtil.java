package com.wzkris.common.security.utils;

import com.wzkris.common.core.exception.user.UserException;
import com.wzkris.common.core.model.CorePrincipal;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 安全工具
 * @create : 2024/04/22 12:22
 * @update : 2024/12/20 16:35
 */
public abstract class SecurityUtil {

    /**
     * 获得当前认证信息，可能登录可能未登录
     *
     * @return 认证信息
     */
    @Nullable
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 设置当前认证信息
     */
    public static void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 是否认证
     */
    public static boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CorePrincipal;
    }

    /**
     * 获取请求的token
     */
    public static String getTokenValue() {
        try {
            return getAuthentication().getCredentials().toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前登录用户信息,未登录抛出异常
     *
     * @return 当前用户
     */
    public static CorePrincipal getPrincipal() {
        try {
            return (CorePrincipal) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new UserException(401, "forbidden.accessDenied.tokenExpired");
        }
    }

    /**
     * 获取当前ID
     *
     * @return 登录ID
     */
    public static Long getId() {
        return getPrincipal().getId();
    }

    /**
     * 获取当前名称
     *
     * @return 登录名称
     */
    public static String getName() {
        return getPrincipal().getName();
    }

    /**
     * 获取当前认证类型,未登录抛出异常
     *
     * @return 登录类型
     */
    public static String getAuthType() {
        return getPrincipal().getType();
    }

    /**
     * 获取权限
     */
    public static Collection<String> getAuthorities() {
        return getPrincipal().getPermissions();
    }

}

package com.wzkris.common.security.utils;

import com.wzkris.common.core.exception.token.TokenExpiredException;
import com.wzkris.common.core.model.ClientPrincipal;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

import java.util.Set;

public final class ClientUtil {

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
    public static ClientPrincipal getClientPrincipal() {
        try {
            Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
            return (ClientPrincipal) authentication.getPrincipal();
        } catch (Exception e) {
            throw new TokenExpiredException(401, "forbidden.accessDenied.tokenExpired");
        }
    }

    /**
     * 获取当前登录用户权限,未登录抛出异常
     *
     * @return 当前用户
     */
    public static Set<String> getScope() {
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
                && authentication.getPrincipal() instanceof ClientPrincipal;
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

}

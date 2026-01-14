package com.wzkris.common.security.utils;

import com.wzkris.common.core.exception.token.TokenExpiredException;
import com.wzkris.common.core.model.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

import java.util.Collection;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 安全工具
 * @create : 2024/04/22 12:22
 * @update : 2024/12/20 16:35
 */
public abstract class SecurityUtil {

    private static final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    /**
     * 获取当前登录用户信息,未登录抛出异常
     *
     * @return 当前用户
     */
    public static UserPrincipal getPrincipal() {
        try {
            Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
            return (UserPrincipal) authentication.getPrincipal();
        } catch (Exception e) {
            throw new TokenExpiredException(401, "forbidden.accessDenied.tokenExpired");
        }
    }

    /**
     * 设置当前认证信息
     */
    public static void setPrincipal(UserPrincipal userPrincipal) {
        SecurityContext context = securityContextHolderStrategy.getContext();

        Authentication currentAuth = context.getAuthentication();
        Object credentials = currentAuth != null ? currentAuth.getCredentials() : null;
        Object details = currentAuth != null ? currentAuth.getDetails() : null;

        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(
                userPrincipal,
                credentials,
                AuthorityUtils.createAuthorityList(userPrincipal.getPerms())
        );
        authentication.setDetails(details);

        context.setAuthentication(authentication);
    }

    /**
     * 是否认证
     */
    public static boolean isAuthenticated() {
        Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
        return authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserPrincipal;
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
    public static Long getId() {
        return getPrincipal().getId();
    }

    /**
     * 获取当前标签
     *
     * @return 标签
     */
    public static String getHint() {
        return getPrincipal().getHint();
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
    public static Collection<String> getPerms() {
        return getPrincipal().getPerms();
    }

}

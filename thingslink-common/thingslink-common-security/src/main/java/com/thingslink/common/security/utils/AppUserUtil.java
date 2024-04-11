package com.thingslink.common.security.utils;

import com.thingslink.common.core.exception.user.UserException;
import com.thingslink.common.security.model.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;

import java.util.Collection;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 顾客用户信息工具类
 * @date : 2024/04/09 16:18
 */
@Slf4j
public class AppUserUtil {

    /**
     * 获得当前认证信息，可能登录可能未登录
     *
     * @return 认证信息
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取权限
     */
    public static Collection<? extends GrantedAuthority> getAuthorities() {
        Authentication authentication = getAuthentication();
        return authentication == null ? AuthorityUtils.NO_AUTHORITIES : authentication.getAuthorities();
    }

    /**
     * 是否登录
     *
     * @description 不能为匿名用户也不能为OAUTH2客户端
     */
    public static boolean isLogin() {
        Authentication authentication = getAuthentication();
        return null != authentication && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)
                && !(authentication instanceof OAuth2ClientAuthenticationToken)
                && authentication.getPrincipal() instanceof AppUser;
    }

    /**
     * 获取当前登录用户信息,未登录抛出异常
     *
     * @return 当前用户
     */
    public static AppUser getAppUser() {
        AppUser appUser;
        try {
            appUser = (AppUser) getAuthentication().getPrincipal();
        }
        catch (Exception e) {
            throw new UserException(401, "user.not.login");
        }
        return appUser;
    }

    /**
     * 获取当前登录用户ID,未登录抛出异常
     *
     * @return 当前用户ID
     */
    public static Long getUserId() {
        return getAppUser().getUserId();
    }
}

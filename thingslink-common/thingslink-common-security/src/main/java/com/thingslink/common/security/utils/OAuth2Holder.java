package com.thingslink.common.security.utils;


import com.thingslink.common.core.exception.user.UserException;
import com.thingslink.common.security.oauth2.model.OAuth2User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 当前登录信息工具类
 * @date : 2024/04/22 12:22
 */
public class OAuth2Holder {

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
     * 是否认证
     *
     * @description 不能为匿名用户也不能为OAUTH2客户端
     */
    public static boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return null != authentication && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.getPrincipal() instanceof OAuth2User;
    }

    /**
     * 获取当前登录用户信息,未登录抛出异常
     *
     * @return 当前用户
     */
    public static OAuth2User getPrincipal() {
        OAuth2User user;
        try {
            user = (OAuth2User) getAuthentication().getPrincipal();
        }
        catch (Exception e) {
            throw new UserException(401, "user.not.login");
        }
        return user;
    }
}

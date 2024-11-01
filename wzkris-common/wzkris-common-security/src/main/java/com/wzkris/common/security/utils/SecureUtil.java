package com.wzkris.common.security.utils;


import com.wzkris.common.core.exception.user.UserException;
import com.wzkris.common.security.oauth2.domain.WzUser;
import com.wzkris.common.security.oauth2.enums.UserType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 安全工具
 * @date : 2024/04/22 12:22
 */
public class SecureUtil {

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
     */
    public static boolean isAuthenticated() {
        try {
            Authentication authentication = getAuthentication();
            return authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取当前登录用户信息,未登录抛出异常
     *
     * @return 当前用户
     */
    public static WzUser getWzUser() {
        try {
            return (WzUser) getAuthentication().getPrincipal();
        }
        catch (Exception e) {
            throw new UserException(401, "user.not.login");
        }
    }

    /**
     * 获取当前登录类型,未登录抛出异常
     *
     * @return 登录类型
     */
    public static UserType getUserType() {
        return getWzUser().getUserType();
    }
}

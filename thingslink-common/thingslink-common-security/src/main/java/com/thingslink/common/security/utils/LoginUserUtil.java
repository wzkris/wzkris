package com.thingslink.common.security.utils;

import com.thingslink.common.core.constant.SecurityConstants;
import com.thingslink.common.core.exception.user.UserException;
import com.thingslink.common.security.model.LoginUser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 用户信息工具类
 * @date : 2023/11/21 14:58
 */
@Slf4j
@Component("LoginUserUtil") //加入Spring容器以用于SPEL
public class LoginUserUtil {

    @Getter
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
                && authentication.getPrincipal() instanceof LoginUser;
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPass   真实密码
     * @param encryPass 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPass, String encryPass) {
        return passwordEncoder.matches(rawPass, encryPass);
    }

    /**
     * 获取当前登录用户信息,未登录抛出异常
     *
     * @return 当前用户
     */
    public static LoginUser getLoginUser() {
        LoginUser loginUser;
        try {
            loginUser = (LoginUser) getAuthentication().getPrincipal();
        }
        catch (Exception e) {
            throw new UserException(401, "user.not.login");
        }
        return loginUser;
    }

    /**
     * 获取当前登录用户ID,未登录抛出异常
     *
     * @return 当前用户ID
     */
    public static Long getUserId() {
        return getLoginUser().getUserId();
    }

    /**
     * 获取当前租户ID,未登录抛出异常
     *
     * @return 当前租户ID
     */
    public static Long getTenantId() {
        return getLoginUser().getTenantId();
    }

    /**
     * 获取当前用户是否是租户最高管理员 (超级管理员为超级租户的最高管理员)
     *
     * @return 是否
     */
    public static boolean isAdmin() {
        return getLoginUser().getIsAdmin();
    }

    /**
     * 获取当前用户是否是超级租户
     * 方便EL表达式判断权限
     *
     * @return 是否
     */
    public static boolean isSuperTenant() {
        return SecurityConstants.SUPER_TENANT_ID.equals(getTenantId());
    }
}

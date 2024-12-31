package com.wzkris.common.security.utils;

import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.exception.user.UserException;
import com.wzkris.common.security.oauth2.domain.model.LoginUser;
import com.wzkris.common.security.oauth2.enums.LoginType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 用户信息工具类
 * @date : 2023/11/21 14:58
 * @UPDATE : 2024/04/22 12:22
 */
@Slf4j
@Component("lg")// 加入Spring容器以用于SPEL
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginUserUtil extends SecurityUtil {

    /**
     * 是否登录
     */
    public static boolean isLogin() {
        return isAuthenticated() && getLoginType().equals(LoginType.SYSTEM_USER);
    }

    /**
     * 获取当前登录用户信息,未登录抛出异常
     *
     * @return 当前用户
     */
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        }
        catch (Exception e) {
            throw new UserException(401, "user.not.login");
        }
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
     * 获取当前登录用户名,未登录抛出异常
     *
     * @return 当前用户名
     */
    public static String getUsername() {
        return getLoginUser().getUsername();
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
        return getLoginUser().getAdmin();
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

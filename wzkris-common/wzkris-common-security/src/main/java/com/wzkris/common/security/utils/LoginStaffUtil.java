package com.wzkris.common.security.utils;

import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.exception.user.UserException;
import com.wzkris.common.core.model.domain.LoginStaff;
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
@Component("ls")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginStaffUtil extends SecurityUtil {

    /**
     * 是否登录
     */
    public static boolean isLogin() {
        return isAuthenticated() && getAuthenticatedType().equals(AuthType.STAFF.getValue());
    }

    /**
     * 获取当前登录用户信息,未登录抛出异常
     *
     * @return 当前用户
     */
    public static LoginStaff get() {
        try {
            return (LoginStaff) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new UserException(401, "forbidden.accessDenied.tokenExpired");
        }
    }

    /**
     * 获取当前登录用户名,未登录抛出异常
     *
     * @return 当前用户名
     */
    public static String getStaffName() {
        return get().getStaffName();
    }

    /**
     * 获取当前租户ID,未登录抛出异常
     *
     * @return 当前租户ID
     */
    public static Long getTenantId() {
        return get().getTenantId();
    }

    /**
     * 获取当前用户是否是管理员
     *
     * @return 是否
     */
    public static boolean isAdmin() {
        return get().isAdmin();
    }

}

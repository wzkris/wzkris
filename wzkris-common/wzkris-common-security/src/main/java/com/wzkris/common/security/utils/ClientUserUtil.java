package com.wzkris.common.security.utils;

import com.wzkris.common.core.exception.user.UserException;
import com.wzkris.common.security.oauth2.domain.model.ClientUser;
import com.wzkris.common.security.oauth2.enums.LoginType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 顾客用户信息工具类
 * @date : 2024/04/09 16:18
 * @UPDATE : 2024/04/22 12:22
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientUserUtil extends SecurityUtil {

    /**
     * 是否登录
     */
    public static boolean isLogin() {
        return isAuthenticated() && getLoginType().equals(LoginType.CLIENT_USER);
    }

    /**
     * 获取当前登录用户信息,未登录抛出异常
     *
     * @return 当前用户
     */
    public static ClientUser getClientUser() {
        try {
            return (ClientUser) getAuthentication().getPrincipal();
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
        return getClientUser().getUserId();
    }
}

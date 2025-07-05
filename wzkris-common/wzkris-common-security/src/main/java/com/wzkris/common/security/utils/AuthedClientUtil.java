package com.wzkris.common.security.utils;

import com.wzkris.auth.rmi.domain.AuthedClient;
import com.wzkris.auth.rmi.enums.AuthenticatedType;
import com.wzkris.common.core.exception.user.UserException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 客户端信息工具类
 * @date : 2025/06/18 17:10
 */
@Slf4j
@Component("ac") // 加入Spring容器以用于SPEL
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthedClientUtil extends SecurityUtil {

    /**
     * 是否登录
     */
    public static boolean isLogin() {
        return isAuthenticated() && getAuthenticatedType().equals(AuthenticatedType.CLIENT.getValue());
    }

    /**
     * 获取当前登录客户端信息,未登录抛出异常
     *
     * @return 当前客户端
     */
    public static AuthedClient getClient() {
        try {
            return (AuthedClient) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new UserException(401, "user.not.login");
        }
    }

    /**
     * 获取当前登录客户端ID,未登录抛出异常
     *
     * @return 当前客户端ID
     */
    public static String getClientId() {
        return getClient().getClientId();
    }

}

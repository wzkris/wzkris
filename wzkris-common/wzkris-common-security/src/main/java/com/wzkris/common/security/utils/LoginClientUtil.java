package com.wzkris.common.security.utils;

import com.wzkris.common.core.exception.user.UserException;
import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.model.domain.LoginClient;
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
@Component("lc") // 加入Spring容器以用于SPEL
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginClientUtil extends SecurityUtil {

    /**
     * 是否登录
     */
    public static boolean isLogin() {
        return isAuthenticated() && getAuthenticatedType().equals(AuthType.CLIENT.getValue());
    }

    /**
     * 获取当前登录客户端信息,未登录抛出异常
     *
     * @return 当前客户端
     */
    public static LoginClient get() {
        try {
            return (LoginClient) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new UserException(401, "forbidden.accessDenied.tokenExpired");
        }
    }

    /**
     * 获取当前登录客户端ID,未登录抛出异常
     *
     * @return 当前客户端ID
     */
    public static String getClientId() {
        return get().getClientId();
    }

}

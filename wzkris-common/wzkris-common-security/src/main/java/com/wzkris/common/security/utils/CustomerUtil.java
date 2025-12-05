package com.wzkris.common.security.utils;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.exception.token.TokenExpiredException;
import com.wzkris.common.core.model.domain.LoginCustomer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 顾客用户信息工具类
 * @date : 2024/04/09 16:18
 * @UPDATE : 2024/04/22 12:22
 */
@Slf4j
@Component("cu")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerUtil extends SecurityUtil {

    /**
     * 是否登录
     */
    public static boolean isLogin() {
        return isAuthenticated() && getAuthType().equals(AuthTypeEnum.CUSTOMER);
    }

    /**
     * 获取当前登录用户信息,未登录抛出异常
     *
     * @return 当前用户
     */
    public static LoginCustomer get() {
        try {
            return (LoginCustomer) getPrincipal();
        } catch (Exception e) {
            throw new TokenExpiredException(401, "forbidden.accessDenied.tokenExpired");
        }
    }

    /**
     * 获取当前ID
     *
     * @return 登录ID
     */
    public static Long getId() {
        return get().getId();
    }

    /**
     * 获取当前手机号,未登录抛出异常
     *
     * @return 手机号
     */
    public static String getPhoneNumber() {
        return get().getPhoneNumber();
    }

    /**
     * 获取当前的微信openid,未登录抛出异常
     *
     * @return openid
     */
    public static String getWxopenid() {
        return get().getWxopenid();
    }

}

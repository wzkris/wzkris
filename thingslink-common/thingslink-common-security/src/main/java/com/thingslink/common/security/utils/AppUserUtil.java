package com.thingslink.common.security.utils;

import com.thingslink.common.security.model.AppUser;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 顾客用户信息工具类
 * @date : 2024/04/09 16:18
 * @UPDATE : 2024/04/22 12:22
 */
@Slf4j
public class AppUserUtil extends CurrentUserHolder {

    /**
     * 是否登录
     *
     * @description 不能为匿名用户也不能为OAUTH2客户端
     */
    public static boolean isLogin() {
        return CurrentUserHolder.isAuthenticated() && CurrentUserHolder.getPrincipal() instanceof AppUser;
    }

    /**
     * 获取当前登录用户信息,未登录抛出异常
     *
     * @return 当前用户
     */
    public static AppUser getAppUser() {
        return (AppUser) CurrentUserHolder.getPrincipal();
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

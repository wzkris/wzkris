package com.thingslink.common.security.utils;

import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.security.oauth2.constants.OAuth2Type;
import com.thingslink.common.security.oauth2.domain.model.LoginApper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 顾客用户信息工具类
 * @date : 2024/04/09 16:18
 * @UPDATE : 2024/04/22 12:22
 */
@Slf4j
public class AppUtil extends OAuth2Holder {

    /**
     * 是否登录
     *
     * @description 不能为匿名用户也不能为OAUTH2客户端
     */
    public static boolean isLogin() {
        return OAuth2Holder.isAuthenticated() && OAuth2Holder.getPrincipal().getOauth2Type().equals(OAuth2Type.APP_USER.getValue());
    }

    /**
     * 获取当前登录用户信息,未登录抛出异常
     *
     * @return 当前用户
     */
    public static LoginApper getAppUser() {
        return JsonUtil.parseObject(OAuth2Holder.getPrincipal().getDetails(), LoginApper.class);
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

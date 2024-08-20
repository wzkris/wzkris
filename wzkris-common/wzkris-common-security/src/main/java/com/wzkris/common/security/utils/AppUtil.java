package com.wzkris.common.security.utils;

import com.wzkris.common.core.exception.user.UserException;
import com.wzkris.common.core.utils.json.JsonUtil;
import com.wzkris.common.security.oauth2.constants.OAuth2Type;
import com.wzkris.common.security.oauth2.domain.OAuth2User;
import com.wzkris.common.security.oauth2.domain.model.LoginApper;
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
        return isAuthenticated() && getOauth2Type().equals(OAuth2Type.APP_USER.getValue());
    }

    /**
     * 获取当前登录用户信息,未登录抛出异常
     *
     * @return 当前用户
     */
    public static LoginApper getAppUser() {
        OAuth2User oAuth2User = getPrincipal();
        if (!oAuth2User.getOauth2Type().equals(OAuth2Type.APP_USER.getValue())) {
            log.warn("获取用户信息失败，当前用户不是APP用户");
            throw new UserException(401, "user.not.login");
        }
        return JsonUtil.parseObject(oAuth2User.getPrincipal(), LoginApper.class);
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

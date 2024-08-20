package com.wzkris.common.security.utils;

import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.exception.user.UserException;
import com.wzkris.common.core.utils.json.JsonUtil;
import com.wzkris.common.security.oauth2.constants.OAuth2Type;
import com.wzkris.common.security.oauth2.domain.OAuth2User;
import com.wzkris.common.security.oauth2.domain.model.LoginSyser;
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
@Component("SysUtil") //加入Spring容器以用于SPEL
public class SysUtil extends OAuth2Holder {

    /**
     * 是否登录
     *
     * @description 不能为匿名用户也不能为OAUTH2客户端
     */
    public static boolean isLogin() {
        return isAuthenticated() && getOauth2Type().equals(OAuth2Type.SYS_USER.getValue());
    }

    /**
     * 获取当前登录用户信息,未登录抛出异常
     *
     * @return 当前用户
     */
    public static LoginSyser getLoginSyser() {
        OAuth2User oAuth2User = getPrincipal();
        if (!oAuth2User.getOauth2Type().equals(OAuth2Type.SYS_USER.getValue())) {
            log.warn("获取用户信息失败，当前用户不是系统用户");
            throw new UserException(401, "user.not.login");
        }
        return JsonUtil.parseObject(oAuth2User.getPrincipal(), LoginSyser.class);
    }

    /**
     * 获取当前登录用户ID,未登录抛出异常
     *
     * @return 当前用户ID
     */
    public static Long getUserId() {
        return getLoginSyser().getUserId();
    }

    /**
     * 获取当前登录用户名,未登录抛出异常
     *
     * @return 当前用户名
     */
    public static String getUsername() {
        return getLoginSyser().getUsername();
    }

    /**
     * 获取当前租户ID,未登录抛出异常
     *
     * @return 当前租户ID
     */
    public static Long getTenantId() {
        return getLoginSyser().getTenantId();
    }

    /**
     * 获取当前用户是否是租户最高管理员 (超级管理员为超级租户的最高管理员)
     *
     * @return 是否
     */
    public static boolean isAdministrator() {
        return getLoginSyser().isAdministrator();
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

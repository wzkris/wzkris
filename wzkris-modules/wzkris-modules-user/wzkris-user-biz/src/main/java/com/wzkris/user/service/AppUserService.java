package com.wzkris.user.service;

import com.wzkris.user.domain.AppUser;

public interface AppUserService {

    /**
     * 添加用户
     */
    void insertUser(AppUser user);

    /**
     * 小程序注册
     *
     * @param jscode openid code
     * @param code   手机号code
     */
    void registerByXcx(String jscode, String code);

    /**
     * 公众号注册
     *
     * @param code code
     */
    void registerByGzh(String code);
}

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
     * @param xcxCode jscode
     */
    void registerByXcx(String xcxCode);
}

package com.wzkris.user.service;

import com.wzkris.user.domain.AppUser;

public interface AppUserService {

    /**
     * 添加用户
     */
    void insertUser(AppUser user);
}

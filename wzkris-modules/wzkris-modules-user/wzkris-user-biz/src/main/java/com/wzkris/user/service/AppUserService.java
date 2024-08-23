package com.wzkris.user.service;

import com.wzkris.user.domain.AppUser;

import java.util.List;

public interface AppUserService {
    /**
     * 条件查询列表
     */
    List<AppUser> list(AppUser user);

    /**
     * 添加用户
     */
    void insertUser(AppUser user);
}

package com.wzkris.user.service;

import com.wzkris.user.domain.AppUser;

import java.util.List;

public interface AppUserService {
    List<AppUser> list(AppUser user);
}

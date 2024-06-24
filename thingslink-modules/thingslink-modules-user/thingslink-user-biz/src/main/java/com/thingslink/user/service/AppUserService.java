package com.thingslink.user.service;

import com.thingslink.user.domain.AppUser;

import java.util.List;

public interface AppUserService {
    List<AppUser> list(AppUser user);
}

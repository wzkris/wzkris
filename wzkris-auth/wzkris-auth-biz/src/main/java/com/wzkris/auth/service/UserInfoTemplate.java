package com.wzkris.auth.service;

import com.wzkris.common.security.oauth2.domain.AuthBaseUser;
import com.wzkris.common.security.oauth2.enums.LoginType;
import jakarta.annotation.Nullable;

public abstract class UserInfoTemplate {

    @Nullable
    public AuthBaseUser loadUserByPhoneNumber(String phoneNumber) {
        return null;
    }

    @Nullable
    public AuthBaseUser loadByUsernameAndPassword(String username, String password) {
        return null;
    }

    @Nullable
    public AuthBaseUser loadUserByWechat(String channel, String wxCode) {
        return null;
    }

    public abstract boolean checkLoginType(LoginType loginType);
}

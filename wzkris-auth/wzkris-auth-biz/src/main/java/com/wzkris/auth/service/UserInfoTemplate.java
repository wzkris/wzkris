package com.wzkris.auth.service;

import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.AuthTypeEnum;
import jakarta.annotation.Nullable;

public abstract class UserInfoTemplate {

    @Nullable
    public CommonAuthenticationToken loadUserByPhoneNumber(String phoneNumber) {
        return null;
    }

    @Nullable
    public CommonAuthenticationToken loadByUsernameAndPassword(String username, String password) {
        return null;
    }

    @Nullable
    public CommonAuthenticationToken loadUserByWxXcx(String wxCode, @Nullable String phoneCode) {
        return null;
    }

    public abstract boolean checkAuthType(AuthTypeEnum authType);

}

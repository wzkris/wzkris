package com.wzkris.auth.service;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.UserPrincipal;
import jakarta.annotation.Nullable;

public abstract class UserInfoTemplate {

    @Nullable
    public UserPrincipal loadUserByPhoneNumber(String phoneNumber) {
        return null;
    }

    @Nullable
    public UserPrincipal loadByUsernameAndPassword(String username, String password) {
        return null;
    }

    @Nullable
    public UserPrincipal loadUserByWxXcx(String wxCode, @Nullable String phoneCode) {
        return null;
    }

    public abstract boolean checkAuthType(AuthTypeEnum authType);

}

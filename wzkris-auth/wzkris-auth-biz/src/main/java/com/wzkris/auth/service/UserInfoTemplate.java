package com.wzkris.auth.service;

import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.model.MyPrincipal;
import jakarta.annotation.Nullable;

public abstract class UserInfoTemplate {

    @Nullable
    public MyPrincipal loadUserByPhoneNumber(String phoneNumber) {
        return null;
    }

    @Nullable
    public MyPrincipal loadByUsernameAndPassword(String username, String password) {
        return null;
    }

    @Nullable
    public MyPrincipal loadUserByWechat(String identifierType, String wxCode) {
        return null;
    }

    public abstract boolean checkAuthType(AuthType authType);

}

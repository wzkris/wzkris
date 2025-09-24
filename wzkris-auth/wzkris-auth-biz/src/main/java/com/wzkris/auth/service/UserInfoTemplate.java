package com.wzkris.auth.service;

import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.security.enums.AuthType;
import jakarta.annotation.Nullable;

public abstract class UserInfoTemplate {

    @Nullable
    public CorePrincipal loadUserByPhoneNumber(String phoneNumber) {
        return null;
    }

    @Nullable
    public CorePrincipal loadByUsernameAndPassword(String username, String password) {
        return null;
    }

    @Nullable
    public CorePrincipal loadUserByWechat(String identifierType, String wxCode) {
        return null;
    }

    public abstract boolean checkAuthType(AuthType authType);

}

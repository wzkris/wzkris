package com.wzkris.auth.service;

import com.wzkris.auth.feign.enums.AuthenticatedType;
import com.wzkris.common.core.domain.CorePrincipal;
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

    public abstract boolean checkAuthenticatedType(AuthenticatedType authenticatedType);

}

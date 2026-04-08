package com.wzkris.auth.service;

import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.AuthTypeEnum;
import jakarta.annotation.Nullable;

public interface LoginUserService {

    @Nullable
    default CommonAuthenticationToken loadUserByPhoneNumber(String phoneNumber) {
        return null;
    }

    @Nullable
    default CommonAuthenticationToken loadByUsernameAndPassword(String username, String password) {
        return null;
    }

    @Nullable
    default CommonAuthenticationToken loadUserByWxXcx(String wxCode, @Nullable String phoneCode) {
        return null;
    }

    boolean checkAuthType(AuthTypeEnum authType);

}

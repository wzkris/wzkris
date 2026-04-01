package com.wzkris.auth.service;

import com.wzkris.common.core.enums.AuthTypeEnum;
import jakarta.annotation.Nullable;

public interface LoginUserService {

    @Nullable
    default Object loadUserByPhoneNumber(String phoneNumber) {
        return null;
    }

    @Nullable
    default Object loadByUsernameAndPassword(String username, String password) {
        return null;
    }

    @Nullable
    default Object loadUserByWxXcx(String wxCode, @Nullable String phoneCode) {
        return null;
    }

    boolean checkAuthType(AuthTypeEnum authType);

}

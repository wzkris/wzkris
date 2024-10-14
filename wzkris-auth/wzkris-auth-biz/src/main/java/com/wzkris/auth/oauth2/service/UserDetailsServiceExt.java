package com.wzkris.auth.oauth2.service;

import com.wzkris.common.core.exception.BusinessExceptionI18n;
import com.wzkris.common.security.oauth2.domain.WzUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserDetailsServiceExt extends UserDetailsService {

    default WzUser loadUserByUsername(String username) {
        throw new BusinessExceptionI18n("oauth2.unsupport.granttype");
    }

    default WzUser loadUserByPhoneNumber(String phoneNumber) {
        throw new BusinessExceptionI18n("oauth2.unsupport.granttype");
    }
}

package com.wzkris.auth.oauth2.service;

import com.wzkris.auth.oauth2.constants.OAuth2GrantTypeConstant;
import com.wzkris.common.security.oauth2.domain.WzUser;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import jakarta.annotation.Nullable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;

public interface UserDetailsServiceExt extends UserDetailsService {

    @Nullable
    default WzUser loadUserByUsername(String username) {
        OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE, "oauth2.unsupport.granttype", AuthorizationGrantType.PASSWORD);
        return null;// never run this line
    }

    @Nullable
    default WzUser loadUserByPhoneNumber(String phoneNumber) {
        OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE, "oauth2.unsupport.granttype", OAuth2GrantTypeConstant.SMS);
        return null;// never run this line
    }
}

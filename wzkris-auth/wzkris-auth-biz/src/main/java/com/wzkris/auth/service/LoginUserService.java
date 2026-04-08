package com.wzkris.auth.service;

import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.web.utils.UserAgentUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import nl.basjes.parse.useragent.UserAgent;
import org.springframework.http.HttpHeaders;

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

    default String getUserAgent(HttpServletRequest request) {
        UserAgent.ImmutableUserAgent userAgent = UserAgentUtil.INSTANCE.parse(request.getHeader(HttpHeaders.USER_AGENT));
        return userAgent.getUserAgentString();
    }

}

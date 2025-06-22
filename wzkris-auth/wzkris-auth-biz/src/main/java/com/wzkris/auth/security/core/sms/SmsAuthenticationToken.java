package com.wzkris.auth.security.core.sms;

import com.wzkris.auth.rmi.enums.AuthenticatedType;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.domain.CorePrincipal;
import lombok.Getter;
import org.springframework.security.core.Transient;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 短信验证token
 */
@Getter
@Transient
public final class SmsAuthenticationToken extends CommonAuthenticationToken {

    private final AuthenticatedType authenticatedType;

    private final String phoneNumber;

    private final String smsCode;

    public SmsAuthenticationToken(
            AuthenticatedType authenticatedType,
            String phoneNumber,
            String smsCode) {
        super(null);
        this.authenticatedType = authenticatedType;
        this.phoneNumber = phoneNumber;
        this.smsCode = smsCode;
    }

    public SmsAuthenticationToken(
            AuthenticatedType authenticatedType,
            String phoneNumber,
            CorePrincipal principal) {
        super(null, principal);
        this.authenticatedType = authenticatedType;
        this.phoneNumber = phoneNumber;
        this.smsCode = null;
    }

    @Override
    public Object getCredentials() {
        return this.smsCode;
    }

}

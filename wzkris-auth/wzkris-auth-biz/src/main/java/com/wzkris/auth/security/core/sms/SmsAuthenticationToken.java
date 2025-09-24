package com.wzkris.auth.security.core.sms;

import com.wzkris.auth.security.constants.OAuth2LoginTypeConstant;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.security.enums.AuthType;
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

    private final AuthType authType;

    private final String phoneNumber;

    private final String smsCode;

    public SmsAuthenticationToken(
            AuthType authType,
            String phoneNumber,
            String smsCode) {
        super(null);
        this.authType = authType;
        this.phoneNumber = phoneNumber;
        this.smsCode = smsCode;
    }

    public SmsAuthenticationToken(
            AuthType authType,
            String phoneNumber,
            CorePrincipal principal) {
        super(null, principal);
        this.authType = authType;
        this.phoneNumber = phoneNumber;
        this.smsCode = null;
    }

    @Override
    public Object getCredentials() {
        return this.smsCode;
    }

    @Override
    public String getLoginType() {
        return OAuth2LoginTypeConstant.SMS;
    }

}

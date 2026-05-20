package com.wzkris.auth.security.core.sms;

import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.Getter;
import org.springframework.security.core.Transient;

/**
 * 短信验证 token（未认证状态，用于接收请求参数）
 */
@Getter
@Transient
public final class SmsAuthenticationToken extends CommonAuthenticationToken {

    private final String phoneNumber;

    private final String smsCode;

    private SmsAuthenticationToken(AuthTypeEnum authType, String phoneNumber, String smsCode) {
        super(authType);
        this.phoneNumber = phoneNumber;
        this.smsCode = smsCode;
    }

    public static SmsAuthenticationToken unauthenticated(
            AuthTypeEnum authType, String phoneNumber, String smsCode) {
        return new SmsAuthenticationToken(authType, phoneNumber, smsCode);
    }

    @Override
    public Object getCredentials() {
        return smsCode;
    }

    @Override
    public Object getPrincipal() {
        return phoneNumber;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

}

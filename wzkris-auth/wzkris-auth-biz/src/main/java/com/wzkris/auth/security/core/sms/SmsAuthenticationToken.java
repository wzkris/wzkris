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

    private final String wxCode;

    private final String appid;

    private SmsAuthenticationToken(AuthTypeEnum authType, String phoneNumber, String smsCode, String wxCode, String appid) {
        super(authType);
        this.phoneNumber = phoneNumber;
        this.smsCode = smsCode;
        this.wxCode = wxCode;
        this.appid = appid;
    }

    public static SmsAuthenticationToken unauthenticated(
            AuthTypeEnum authType, String phoneNumber, String smsCode, String wxCode, String appid) {
        return new SmsAuthenticationToken(authType, phoneNumber, smsCode, wxCode, appid);
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

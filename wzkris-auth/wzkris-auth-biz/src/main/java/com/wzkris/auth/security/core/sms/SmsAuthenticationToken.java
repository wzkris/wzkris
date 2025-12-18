package com.wzkris.auth.security.core.sms;

import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.MyPrincipal;
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

    private final AuthTypeEnum authType;

    private final String phoneNumber;

    private final String smsCode;

    public SmsAuthenticationToken(
            AuthTypeEnum authType,
            String phoneNumber,
            String smsCode) {
        super(null);
        this.authType = authType;
        this.phoneNumber = phoneNumber;
        this.smsCode = smsCode;
    }

    public SmsAuthenticationToken(
            AuthTypeEnum authType,
            String phoneNumber,
            MyPrincipal principal) {
        super(null, principal);
        this.authType = authType;
        this.phoneNumber = phoneNumber;
        this.smsCode = null;
    }

    @Override
    public LoginTypeEnum getLoginType() {
        return LoginTypeEnum.SMS;
    }

}

package com.wzkris.auth.security.core.sms;

import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.UserPrincipal;
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

    private SmsAuthenticationToken(
            AuthTypeEnum authType,
            String phoneNumber,
            String smsCode) {
        super(null);
        this.authType = authType;
        this.phoneNumber = phoneNumber;
        this.smsCode = smsCode;
    }

    private SmsAuthenticationToken(
            AuthTypeEnum authType,
            String phoneNumber,
            UserPrincipal principal) {
        super(principal);
        this.authType = authType;
        this.phoneNumber = phoneNumber;
        this.smsCode = null;
    }

    /**
     * 创建未认证状态的Token（用于接收请求参数）
     */
    public static SmsAuthenticationToken unauthenticated(
            AuthTypeEnum authType,
            String phoneNumber,
            String smsCode) {
        return new SmsAuthenticationToken(authType, phoneNumber, smsCode);
    }

    /**
     * 创建已认证状态的Token（认证成功后使用）
     */
    public static SmsAuthenticationToken authenticated(
            AuthTypeEnum authType,
            String phoneNumber,
            UserPrincipal principal) {
        return new SmsAuthenticationToken(authType, phoneNumber, principal);
    }

    @Override
    public LoginTypeEnum getLoginType() {
        return LoginTypeEnum.SMS;
    }

}

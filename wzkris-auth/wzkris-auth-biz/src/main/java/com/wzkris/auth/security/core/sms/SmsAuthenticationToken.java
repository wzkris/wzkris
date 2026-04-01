package com.wzkris.auth.security.core.sms;

import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Transient;

import java.util.Collections;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 短信验证token（未认证状态，用于接收请求参数）
 */
@Getter
@Transient
public final class SmsAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthTypeEnum authType;

    private final String phoneNumber;

    private final String smsCode;

    private SmsAuthenticationToken(
            AuthTypeEnum authType,
            String phoneNumber,
            String smsCode) {
        super(Collections.emptyList());
        this.authType = authType;
        this.phoneNumber = phoneNumber;
        this.smsCode = smsCode;
        super.setAuthenticated(false);
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

    @Override
    public Object getCredentials() {
        return smsCode;
    }

    @Override
    public Object getPrincipal() {
        return phoneNumber;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

}

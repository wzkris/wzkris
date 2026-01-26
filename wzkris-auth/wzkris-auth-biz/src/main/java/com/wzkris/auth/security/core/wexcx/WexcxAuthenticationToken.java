package com.wzkris.auth.security.core.wexcx;

import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Transient;

import java.util.Collections;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 微信小程序验证token（未认证状态，用于接收请求参数）
 */
@Getter
@Transient
public final class WexcxAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthTypeEnum authType;

    private final String wxCode;

    private final String phoneCode;

    private WexcxAuthenticationToken(
            AuthTypeEnum authType,
            String wxCode,
            String phoneCode) {
        super(Collections.emptyList());
        this.authType = authType;
        this.wxCode = wxCode;
        this.phoneCode = phoneCode;
        super.setAuthenticated(false);
    }

    /**
     * 创建未认证状态的Token（用于接收请求参数）
     */
    public static WexcxAuthenticationToken unauthenticated(
            AuthTypeEnum authType,
            String wxCode,
            String phoneCode) {
        return new WexcxAuthenticationToken(authType, wxCode, phoneCode);
    }

    @Override
    public Object getCredentials() {
        return phoneCode;
    }

    @Override
    public Object getPrincipal() {
        return wxCode;
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

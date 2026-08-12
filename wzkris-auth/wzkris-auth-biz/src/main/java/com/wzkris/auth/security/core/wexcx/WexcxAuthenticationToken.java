package com.wzkris.auth.security.core.wexcx;

import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.Getter;
import org.springframework.security.core.Transient;

/**
 * 微信小程序验证 token（未认证状态，用于接收请求参数）
 */
@Getter
@Transient
public final class WexcxAuthenticationToken extends CommonAuthenticationToken {

    private final String socialType;

    private final String wxCode;

    private final String phoneCode;

    private final String appid;

    private WexcxAuthenticationToken(AuthTypeEnum authType, String socialType, String wxCode, String phoneCode, String appid) {
        super(authType);
        this.socialType = socialType;
        this.wxCode = wxCode;
        this.phoneCode = phoneCode;
        this.appid = appid;
    }

    public static WexcxAuthenticationToken unauthenticated(
            AuthTypeEnum authType, String socialType, String wxCode, String phoneCode, String appid) {
        return new WexcxAuthenticationToken(authType, socialType, wxCode, phoneCode, appid);
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
    public void eraseCredentials() {
        super.eraseCredentials();
    }

}

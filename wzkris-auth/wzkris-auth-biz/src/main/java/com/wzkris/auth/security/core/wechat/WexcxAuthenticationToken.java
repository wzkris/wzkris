package com.wzkris.auth.security.core.wechat;

import com.wzkris.auth.security.constants.OAuth2LoginTypeConstant;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.model.MyPrincipal;
import lombok.Getter;
import org.springframework.security.core.Transient;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 微信小程序验证token
 */
@Getter
@Transient
public final class WexcxAuthenticationToken extends CommonAuthenticationToken {

    private final AuthType authType;

    private final String wxCode;

    private final String phoneCode;

    public WexcxAuthenticationToken(
            AuthType authType,
            String wxCode,
            String phoneCode) {
        super(null);
        this.authType = authType;
        this.wxCode = wxCode;
        this.phoneCode = phoneCode;
    }

    public WexcxAuthenticationToken(
            AuthType authType,
            MyPrincipal principal) {
        super(null, principal);
        this.authType = authType;
        this.wxCode = null;
        this.phoneCode = null;
    }

    @Override
    public Object getCredentials() {
        return this.wxCode;
    }

    @Override
    public String getLoginType() {
        return OAuth2LoginTypeConstant.WE_XCX;
    }

}

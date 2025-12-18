package com.wzkris.auth.security.core.wexcx;

import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.AuthTypeEnum;
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

    private final AuthTypeEnum authType;

    private final String wxCode;

    private final String phoneCode;

    public WexcxAuthenticationToken(
            AuthTypeEnum authType,
            String wxCode,
            String phoneCode) {
        super(null);
        this.authType = authType;
        this.wxCode = wxCode;
        this.phoneCode = phoneCode;
    }

    public WexcxAuthenticationToken(
            AuthTypeEnum authType,
            MyPrincipal principal) {
        super(null, principal);
        this.authType = authType;
        this.wxCode = null;
        this.phoneCode = null;
    }

    @Override
    public LoginTypeEnum getLoginType() {
        return LoginTypeEnum.WE_XCX;
    }

}

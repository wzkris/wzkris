package com.wzkris.auth.security.core.wexcx;

import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.UserPrincipal;
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

    private WexcxAuthenticationToken(
            AuthTypeEnum authType,
            String wxCode,
            String phoneCode) {
        super(null);
        this.authType = authType;
        this.wxCode = wxCode;
        this.phoneCode = phoneCode;
    }

    private WexcxAuthenticationToken(
            AuthTypeEnum authType,
            UserPrincipal principal) {
        super(principal);
        this.authType = authType;
        this.wxCode = null;
        this.phoneCode = null;
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

    /**
     * 创建已认证状态的Token（认证成功后使用）
     */
    public static WexcxAuthenticationToken authenticated(
            AuthTypeEnum authType,
            UserPrincipal principal) {
        return new WexcxAuthenticationToken(authType, principal);
    }

    @Override
    public LoginTypeEnum getLoginType() {
        return LoginTypeEnum.WE_XCX;
    }

}

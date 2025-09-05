package com.wzkris.auth.security.core.wechat;

import com.wzkris.auth.feign.enums.AuthenticatedType;
import com.wzkris.auth.security.constants.OAuth2LoginTypeConstant;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.domain.CorePrincipal;
import lombok.Getter;
import org.springframework.security.core.Transient;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 短信验证token
 */
@Getter
@Transient
public final class WechatAuthenticationToken extends CommonAuthenticationToken {

    private final AuthenticatedType authenticatedType;

    private final String identifierType;

    private final String wxCode;

    public WechatAuthenticationToken(
            AuthenticatedType authenticatedType,
            String identifierType,
            String wxCode) {
        super(null);
        this.authenticatedType = authenticatedType;
        this.identifierType = identifierType;
        this.wxCode = wxCode;
    }

    public WechatAuthenticationToken(
            AuthenticatedType authenticatedType,
            String identifierType,
            CorePrincipal principal) {
        super(null, principal);
        this.authenticatedType = authenticatedType;
        this.identifierType = identifierType;
        this.wxCode = null;
    }

    @Override
    public Object getCredentials() {
        return this.wxCode;
    }

    @Override
    public String getLoginType() {
        return OAuth2LoginTypeConstant.WECHAT;
    }

}

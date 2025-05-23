package com.wzkris.auth.oauth2.core.wechat;

import com.wzkris.auth.oauth2.constants.OAuth2GrantTypeConstant;
import com.wzkris.auth.oauth2.core.CommonAuthenticationToken;
import com.wzkris.common.security.oauth2.enums.LoginType;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.Transient;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Set;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 短信验证token
 */
@Getter
@Transient
public final class WechatAuthenticationToken extends CommonAuthenticationToken {

    private final LoginType loginType;

    private final String identifierType;

    private final String wxCode;

    public WechatAuthenticationToken(LoginType loginType,
                                     String identifierType,
                                     String wxCode,
                                     Authentication clientPrincipal,
                                     Set<String> scopes,
                                     Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(OAuth2GrantTypeConstant.WECHAT), clientPrincipal, scopes, additionalParameters);
        Assert.notNull(loginType, "loginType cannot be null");
        Assert.notNull(identifierType, "identifierType cannot be null");
        Assert.notNull(wxCode, "wxCode cannot be null");
        this.loginType = loginType;
        this.identifierType = identifierType;
        this.wxCode = wxCode;
    }
}

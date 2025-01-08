package com.wzkris.auth.oauth2.core.wechat;

import cn.hutool.core.util.ObjUtil;
import com.wzkris.auth.oauth2.constants.OAuth2ParameterConstant;
import com.wzkris.auth.oauth2.core.CommonAuthenticationProvider;
import com.wzkris.auth.service.ClientUserService;
import com.wzkris.auth.service.LoginUserService;
import com.wzkris.common.security.oauth2.domain.AuthBaseUser;
import com.wzkris.common.security.oauth2.enums.LoginType;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 短信模式核心处理
 */
@Component
public final class WechatAuthenticationProvider extends CommonAuthenticationProvider<WechatAuthenticationToken> {

    private final LoginUserService loginUserService;
    private final ClientUserService clientUserService;

    public WechatAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                        LoginUserService loginUserService,
                                        ClientUserService clientUserService,
                                        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        super(tokenGenerator, authorizationService);
        this.loginUserService = loginUserService;
        this.clientUserService = clientUserService;
    }

    @Override
    public UsernamePasswordAuthenticationToken doAuthenticate(Authentication authentication) {
        WechatAuthenticationToken authenticationToken = (WechatAuthenticationToken) authentication;

        AuthBaseUser baseUser;
        if (ObjUtil.equals(LoginType.SYSTEM_USER, authenticationToken.getLoginType())) {
            baseUser = loginUserService.getUserByWechat(authenticationToken.getChannel(), authenticationToken.getWxCode());
        }
        else if (ObjUtil.equals(LoginType.CLIENT_USER, authenticationToken.getLoginType())) {
            baseUser = clientUserService.getUserByWechat(authenticationToken.getChannel(), authenticationToken.getWxCode());
        }
        else {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "request.param.error", OAuth2ParameterConstant.USER_TYPE);
            return null;// never run this line
        }

        if (baseUser == null) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.smslogin.fail");
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(baseUser, null, null);
        usernamePasswordAuthenticationToken.setDetails(authenticationToken.getDetails());
        return usernamePasswordAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

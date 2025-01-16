package com.wzkris.auth.oauth2.core.wechat;

import com.wzkris.auth.oauth2.constants.OAuth2ParameterConstant;
import com.wzkris.auth.oauth2.core.CommonAuthenticationProvider;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.security.oauth2.domain.AuthBaseUser;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 短信模式核心处理
 */
@Component
public final class WechatAuthenticationProvider extends CommonAuthenticationProvider<WechatAuthenticationToken> {

    private final List<UserInfoTemplate> userInfoTemplates;

    public WechatAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                        List<UserInfoTemplate> userInfoTemplates) {
        super(authorizationService, tokenGenerator);
        this.userInfoTemplates = userInfoTemplates;
    }

    @Override
    public UsernamePasswordAuthenticationToken doAuthenticate(Authentication authentication) {
        WechatAuthenticationToken authenticationToken = (WechatAuthenticationToken) authentication;

        Optional<UserInfoTemplate> templateOptional = userInfoTemplates.stream()
                .filter(t -> t.checkLoginType(authenticationToken.getLoginType()))
                .findFirst();

        if (templateOptional.isEmpty()) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST,
                    "request.param.error", OAuth2ParameterConstant.USER_TYPE);
            return null;// never run this line
        }

        AuthBaseUser baseUser = templateOptional.get().loadUserByWechat(authenticationToken.getChannel(), authenticationToken.getWxCode());

        if (baseUser == null) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.wxlogin.fail");
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

package com.wzkris.auth.oauth2.core.wechat;

import com.wzkris.auth.oauth2.constants.OAuth2GrantTypeConstant;
import com.wzkris.auth.oauth2.constants.OAuth2ParameterConstant;
import com.wzkris.auth.oauth2.core.CommonAuthenticationConverter;
import com.wzkris.auth.oauth2.core.CommonAuthenticationToken;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.oauth2.enums.LoginType;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 微信登录模式转换器
 */
public final class WechatAuthenticationConverter extends CommonAuthenticationConverter<CommonAuthenticationToken> {

    @Override
    protected boolean support(String grantType) {
        return OAuth2GrantTypeConstant.WECHAT.equals(grantType);
    }

    @Override
    public void checkParams(MultiValueMap<String, String> parameters) {
        // phonenumber (REQUIRED)
        String channel = parameters.getFirst(OAuth2ParameterConstant.CHANNEL);
        if (!StringUtils.hasText(channel) || parameters.get(OAuth2ParameterConstant.CHANNEL).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.wxlogin.fail", OAuth2ParameterConstant.CHANNEL);
        }

        // wxcode (REQUIRED)
        String wxCode = parameters.getFirst(OAuth2ParameterConstant.WX_CODE);
        if (!StringUtils.hasText(wxCode) || parameters.get(OAuth2ParameterConstant.WX_CODE).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.wxlogin.fail", OAuth2ParameterConstant.WX_CODE);
        }

        // userType (REQUIRED)
        String userType = parameters.getFirst(OAuth2ParameterConstant.USER_TYPE);
        if (!StringUtils.hasText(userType) || parameters.get(OAuth2ParameterConstant.USER_TYPE).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.wxlogin.fail", OAuth2ParameterConstant.USER_TYPE);
        }
    }

    @Override
    public CommonAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        String channel = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterConstant.CHANNEL));
        String wxCode = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterConstant.WX_CODE));
        String userType = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterConstant.USER_TYPE));
        LoginType loginTypeEm;
        try {
            loginTypeEm = LoginType.valueOf(userType);
        }
        catch (Exception e) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "request.param.error", OAuth2ParameterConstant.USER_TYPE);
            return null;// never run this line
        }
        return new WechatAuthenticationToken(loginTypeEm, channel, wxCode,
                clientPrincipal, requestedScopes, null);
    }

}

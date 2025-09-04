package com.wzkris.auth.security.core.wechat;

import com.wzkris.auth.enums.BizLoginCode;
import com.wzkris.auth.rmi.enums.AuthenticatedType;
import com.wzkris.auth.security.constants.OAuth2LoginTypeConstant;
import com.wzkris.auth.security.constants.OAuth2ParameterConstant;
import com.wzkris.auth.security.core.CommonAuthenticationConverter;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 微信登录模式转换器
 */
@Component
public final class WechatAuthenticationConverter extends CommonAuthenticationConverter<CommonAuthenticationToken> {

    @Override
    protected boolean support(String loginType) {
        return OAuth2LoginTypeConstant.WECHAT.equals(loginType);
    }

    @Override
    public void checkParams(MultiValueMap<String, String> parameters) {
        // channel (REQUIRED)
        String channel = parameters.getFirst(OAuth2ParameterConstant.CHANNEL);
        if (!StringUtils.hasText(channel)
                || parameters.get(OAuth2ParameterConstant.CHANNEL).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizBaseCode.MISSING_PARAMETER.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "oauth2.wxlogin.fail",
                    OAuth2ParameterConstant.CHANNEL);
        }

        // wxcode (REQUIRED)
        String wxCode = parameters.getFirst(OAuth2ParameterConstant.WX_CODE);
        if (!StringUtils.hasText(wxCode)
                || parameters.get(OAuth2ParameterConstant.WX_CODE).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizBaseCode.MISSING_PARAMETER.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "oauth2.wxlogin.fail",
                    OAuth2ParameterConstant.WX_CODE);
        }

        // userType (REQUIRED)
        String userType = parameters.getFirst(OAuth2ParameterConstant.USER_TYPE);
        if (!StringUtils.hasText(userType)
                || parameters.get(OAuth2ParameterConstant.USER_TYPE).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizBaseCode.MISSING_PARAMETER.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "oauth2.wxlogin.fail",
                    OAuth2ParameterConstant.USER_TYPE);
        }
    }

    @Override
    protected CommonAuthenticationToken buildToken(String loginType, Map<String, Object> additionalParameters) {
        String channel = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterConstant.CHANNEL));
        String wxCode = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterConstant.WX_CODE));
        String userType = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterConstant.USER_TYPE));
        AuthenticatedType authenticatedType = AuthenticatedType.fromValue(userType);
        if (authenticatedType == null) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCode.PARAMETER_ERROR.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "invalidParameter.param.invalid",
                    OAuth2ParameterConstant.USER_TYPE);
        }
        return new WechatAuthenticationToken(authenticatedType, channel, wxCode);
    }

}

package com.wzkris.auth.security.core.wechat;

import com.wzkris.auth.enums.BizLoginCode;
import com.wzkris.auth.security.constants.OAuth2LoginTypeConstant;
import com.wzkris.auth.security.constants.OAuth2ParameterConstant;
import com.wzkris.auth.security.core.CommonAuthenticationConverter;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.AuthType;
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
 * @description 微信小程序登录模式转换器
 */
@Component
public final class WexcxAuthenticationConverter extends CommonAuthenticationConverter<CommonAuthenticationToken> {

    @Override
    protected boolean support(String loginType) {
        return OAuth2LoginTypeConstant.WE_XCX.equals(loginType);
    }

    @Override
    public void checkParams(MultiValueMap<String, String> parameters) {
        // wxcode (REQUIRED)
        String wxCode = parameters.getFirst(OAuth2ParameterConstant.WXXCX_CODE);
        if (!StringUtils.hasText(wxCode)
                || parameters.get(OAuth2ParameterConstant.WXXCX_CODE).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizBaseCode.REQUEST_ERROR.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "oauth2.wxlogin.fail",
                    OAuth2ParameterConstant.WXXCX_CODE);
        }

        // authType (REQUIRED)
        String authType = parameters.getFirst(OAuth2ParameterConstant.AUTH_TYPE);
        if (!StringUtils.hasText(authType)
                || parameters.get(OAuth2ParameterConstant.AUTH_TYPE).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizBaseCode.REQUEST_ERROR.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "oauth2.wxlogin.fail",
                    OAuth2ParameterConstant.AUTH_TYPE);
        }
    }

    @Override
    protected CommonAuthenticationToken buildToken(String loginType, Map<String, Object> additionalParameters) {
        String type = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterConstant.AUTH_TYPE));
        AuthType authType = AuthType.fromValue(type);
        if (authType == null) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCode.PARAMETER_ERROR.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "invalidParameter.param.invalid",
                    OAuth2ParameterConstant.AUTH_TYPE);
        }
        String wxCode = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterConstant.WXXCX_CODE));
        String phoneCode = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterConstant.WXXCX_PHONE_CODE));
        return new WexcxAuthenticationToken(authType, wxCode, phoneCode);
    }

}

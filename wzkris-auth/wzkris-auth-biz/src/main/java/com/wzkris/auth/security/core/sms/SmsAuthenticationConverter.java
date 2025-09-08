package com.wzkris.auth.security.core.sms;

import com.wzkris.auth.enums.BizLoginCode;
import com.wzkris.auth.feign.enums.AuthType;
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
 * @description 短信模式转换器
 */
@Component
public final class SmsAuthenticationConverter extends CommonAuthenticationConverter<CommonAuthenticationToken> {

    @Override
    protected boolean support(String loginType) {
        return OAuth2LoginTypeConstant.SMS.equals(loginType);
    }

    @Override
    public void checkParams(MultiValueMap<String, String> parameters) {
        // phonenumber (REQUIRED)
        String phoneNumber = parameters.getFirst(OAuth2ParameterConstant.PHONE_NUMBER);
        if (!StringUtils.hasText(phoneNumber)
                || parameters.get(OAuth2ParameterConstant.PHONE_NUMBER).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizBaseCode.MISSING_PARAMETER.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "oauth2.smslogin.fail",
                    OAuth2ParameterConstant.PHONE_NUMBER);
        }

        // smscode (REQUIRED)
        String smsCode = parameters.getFirst(OAuth2ParameterConstant.SMS_CODE);
        if (!StringUtils.hasText(smsCode)
                || parameters.get(OAuth2ParameterConstant.SMS_CODE).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizBaseCode.MISSING_PARAMETER.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "oauth2.smslogin.fail",
                    OAuth2ParameterConstant.SMS_CODE);
        }

        // userType (REQUIRED)
        String userType = parameters.getFirst(OAuth2ParameterConstant.USER_TYPE);
        if (!StringUtils.hasText(userType)
                || parameters.get(OAuth2ParameterConstant.USER_TYPE).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizBaseCode.MISSING_PARAMETER.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "oauth2.smslogin.fail",
                    OAuth2ParameterConstant.USER_TYPE);
        }
    }

    @Override
    protected CommonAuthenticationToken buildToken(String loginType, Map<String, Object> additionalParameters) {
        String phoneNumber = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterConstant.PHONE_NUMBER));
        String smsCode = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterConstant.SMS_CODE));
        String userType = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterConstant.USER_TYPE));
        AuthType authType = AuthType.fromValue(userType);
        if (authType == null) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCode.PARAMETER_ERROR.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "invalidParameter.param.invalid",
                    OAuth2ParameterConstant.USER_TYPE);
        }
        return new SmsAuthenticationToken(authType, phoneNumber, smsCode);
    }

}

package com.wzkris.auth.security.core.sms;

import com.wzkris.auth.constants.OAuth2ParameterConstant;
import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.security.core.CommonAuthenticationConverter;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
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
    protected boolean support(LoginTypeEnum loginType) {
        return LoginTypeEnum.SMS.equals(loginType);
    }

    @Override
    public void checkParams(MultiValueMap<String, String> parameters) {
        // phonenumber (REQUIRED)
        String phoneNumber = parameters.getFirst(OAuth2ParameterConstant.PHONE_NUMBER);
        if (!StringUtils.hasText(phoneNumber)
                || parameters.get(OAuth2ParameterConstant.PHONE_NUMBER).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizBaseCodeEnum.REQUEST_ERROR.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "oauth2.smslogin.fail",
                    OAuth2ParameterConstant.PHONE_NUMBER);
        }

        // smscode (REQUIRED)
        String smsCode = parameters.getFirst(OAuth2ParameterConstant.SMS_CODE);
        if (!StringUtils.hasText(smsCode)
                || parameters.get(OAuth2ParameterConstant.SMS_CODE).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizBaseCodeEnum.REQUEST_ERROR.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "oauth2.smslogin.fail",
                    OAuth2ParameterConstant.SMS_CODE);
        }
    }

    @Override
    protected CommonAuthenticationToken buildToken(AuthTypeEnum authTypeEnum, Map<String, Object> additionalParameters) {
        String phoneNumber = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterConstant.PHONE_NUMBER));
        String smsCode = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterConstant.SMS_CODE));
        return new SmsAuthenticationToken(authTypeEnum, phoneNumber, smsCode);
    }

}

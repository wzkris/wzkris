package com.wzkris.auth.oauth2.core.sms;

import com.wzkris.auth.oauth2.constants.GrantTypeConstant;
import com.wzkris.auth.oauth2.constants.OAuth2ParameterConstant;
import com.wzkris.auth.oauth2.core.CommonAuthenticationConverter;
import com.wzkris.auth.oauth2.core.CommonAuthenticationToken;
import com.wzkris.common.core.utils.StringUtil;
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
 * @description 短信模式转换器
 */
public final class SmsAuthenticationConverter extends CommonAuthenticationConverter<CommonAuthenticationToken> {

    @Override
    protected boolean support(String grantType) {
        return GrantTypeConstant.SMS.equals(grantType);
    }

    @Override
    public void checkParams(MultiValueMap<String, String> parameters) {
        // phonenumber (REQUIRED)
        String phoneNumber = parameters.getFirst(OAuth2ParameterConstant.PHONE_NUMBER);
        if (!StringUtils.hasText(phoneNumber) || parameters.get(OAuth2ParameterConstant.PHONE_NUMBER).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.smslogin.fail", OAuth2ParameterConstant.PHONE_NUMBER);
        }

        // smscode (REQUIRED)
        String smsCode = parameters.getFirst(OAuth2ParameterConstant.SMS_CODE);
        if (!StringUtils.hasText(smsCode) || parameters.get(OAuth2ParameterConstant.SMS_CODE).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.smslogin.fail", OAuth2ParameterConstant.SMS_CODE);
        }
    }

    @Override
    public CommonAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        String phoneNumber = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterConstant.PHONE_NUMBER));
        String smsCode = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterConstant.SMS_CODE));
        return new SmsAuthenticationToken(phoneNumber, smsCode, clientPrincipal, requestedScopes, null);
    }

}

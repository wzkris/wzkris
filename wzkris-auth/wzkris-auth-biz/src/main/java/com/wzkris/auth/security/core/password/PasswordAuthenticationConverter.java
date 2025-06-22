package com.wzkris.auth.security.core.password;

import com.wzkris.auth.security.constants.OAuth2LoginTypeConstant;
import com.wzkris.auth.security.core.CommonAuthenticationConverter;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 密码模式转换器
 */
@Component
public final class PasswordAuthenticationConverter extends CommonAuthenticationConverter<CommonAuthenticationToken> {

    private static final String CAPTCHA_ID = "captcha_id";

    @Override
    protected boolean support(String loginType) {
        return OAuth2LoginTypeConstant.PASSWORD.equals(loginType);
    }

    @Override
    public void checkParams(MultiValueMap<String, String> parameters) {
        // username (REQUIRED)
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (!StringUtils.hasText(username)
                || parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizCode.BAD_REQUEST.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "oauth2.passlogin.fail",
                    OAuth2ParameterNames.USERNAME);
        }

        // password (REQUIRED)
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (!StringUtils.hasText(password)
                || parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizCode.BAD_REQUEST.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "oauth2.passlogin.fail",
                    OAuth2ParameterNames.PASSWORD);
        }

        // captchaId (REQUIRED)
        String captchaId = parameters.getFirst(CAPTCHA_ID);
        if (!StringUtils.hasText(captchaId)
                || parameters.get(CAPTCHA_ID).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizCode.BAD_REQUEST.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "captcha.error",
                    OAuth2ParameterNames.PASSWORD);
        }
    }

    @Override
    protected CommonAuthenticationToken buildToken(String loginType, Map<String, Object> additionalParameters) {
        String username = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterNames.USERNAME));
        String password = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterNames.PASSWORD));
        String captchaId = StringUtil.toStringOrNull(additionalParameters.get(CAPTCHA_ID));
        return new PasswordAuthenticationToken(username, password, captchaId);
    }

}

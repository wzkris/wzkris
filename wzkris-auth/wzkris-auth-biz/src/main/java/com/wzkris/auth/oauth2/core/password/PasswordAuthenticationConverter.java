package com.wzkris.auth.oauth2.core.password;

import com.wzkris.auth.oauth2.core.CommonAuthenticationConverter;
import com.wzkris.auth.oauth2.core.CommonAuthenticationToken;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 密码模式转换器
 */
public final class PasswordAuthenticationConverter extends CommonAuthenticationConverter<CommonAuthenticationToken> {

    private static final String CODE = "code";

    private static final String UUID = "uuid";

    @Override
    protected boolean support(String grantType) {
        return AuthorizationGrantType.PASSWORD.getValue().equals(grantType);
    }

    @Override
    public void checkParams(MultiValueMap<String, String> parameters) {

        // username (REQUIRED)
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (!StringUtils.hasText(username) || parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.passlogin.fail", OAuth2ParameterNames.USERNAME);
        }

        // password (REQUIRED)
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (!StringUtils.hasText(password) || parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.passlogin.fail", OAuth2ParameterNames.PASSWORD);
        }
    }

    @Override
    public CommonAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        String username = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterNames.USERNAME));
        String password = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterNames.PASSWORD));
        String code = StringUtil.toStringOrNull(additionalParameters.get(CODE));
        String uuid = StringUtil.toStringOrNull(additionalParameters.get(UUID));
        return new PasswordAuthenticationToken(username, password, uuid, code, clientPrincipal, requestedScopes, null);
    }

}

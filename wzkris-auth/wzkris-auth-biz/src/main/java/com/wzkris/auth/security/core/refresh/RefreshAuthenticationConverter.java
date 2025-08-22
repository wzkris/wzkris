package com.wzkris.auth.security.core.refresh;

import com.wzkris.auth.security.constants.OAuth2LoginTypeConstant;
import com.wzkris.auth.security.core.CommonAuthenticationConverter;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.BizBaseCode;
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
 * @description 刷新模式转换器
 */
@Component
public final class RefreshAuthenticationConverter extends CommonAuthenticationConverter<RefreshAuthenticationToken> {

    @Override
    protected boolean support(String loginType) {
        return OAuth2LoginTypeConstant.REFRESH.equals(loginType);
    }

    @Override
    public void checkParams(MultiValueMap<String, String> parameters) {
        // username (REQUIRED)
        String refreshToken = parameters.getFirst(OAuth2ParameterNames.REFRESH_TOKEN);
        if (!StringUtils.hasText(refreshToken)
                || parameters.get(OAuth2ParameterNames.REFRESH_TOKEN).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizBaseCode.BAD_REQUEST.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "oauth2.refresh.fail",
                    OAuth2ParameterNames.REFRESH_TOKEN);
        }
    }

    @Override
    protected CommonAuthenticationToken buildToken(String loginType, Map<String, Object> additionalParameters) {
        String refreshToken = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterNames.REFRESH_TOKEN));
        return new RefreshAuthenticationToken(refreshToken);
    }

}

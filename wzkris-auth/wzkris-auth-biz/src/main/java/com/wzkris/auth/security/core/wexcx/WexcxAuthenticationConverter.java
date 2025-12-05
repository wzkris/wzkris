package com.wzkris.auth.security.core.wexcx;

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
 * @description 微信小程序登录模式转换器
 */
@Component
public final class WexcxAuthenticationConverter extends CommonAuthenticationConverter<CommonAuthenticationToken> {

    @Override
    protected boolean support(LoginTypeEnum loginType) {
        return LoginTypeEnum.WE_XCX.equals(loginType);
    }

    @Override
    public void checkParams(MultiValueMap<String, String> parameters) {
        // wxcode (REQUIRED)
        String wxCode = parameters.getFirst(OAuth2ParameterConstant.WXXCX_CODE);
        if (!StringUtils.hasText(wxCode) || parameters.get(OAuth2ParameterConstant.WXXCX_CODE).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(BizBaseCodeEnum.REQUEST_ERROR.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.wxlogin.fail", OAuth2ParameterConstant.WXXCX_CODE);
        }
    }

    @Override
    protected CommonAuthenticationToken buildToken(AuthTypeEnum authTypeEnum, Map<String, Object> additionalParameters) {
        String wxCode = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterConstant.WXXCX_CODE));
        String phoneCode = StringUtil.toStringOrNull(additionalParameters.get(OAuth2ParameterConstant.WXXCX_PHONE_CODE));
        return new WexcxAuthenticationToken(authTypeEnum, wxCode, phoneCode);
    }

}

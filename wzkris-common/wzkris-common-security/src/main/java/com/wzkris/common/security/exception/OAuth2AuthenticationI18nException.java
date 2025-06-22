package com.wzkris.common.security.exception;

import com.wzkris.common.core.utils.I18nUtil;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 自定义OAuth2国际化异常
 * @date : 2024/4/1 14:48:50
 */
public final class OAuth2AuthenticationI18nException extends OAuth2AuthenticationException {

    public OAuth2AuthenticationI18nException(int code, String i18code, Object... args) {
        super(new CustomOAuth2Error(code, I18nUtil.message(i18code, args)));
    }

    public OAuth2AuthenticationI18nException(int code, String errorCode, String uri, String i18code, Object... args) {
        super(new CustomOAuth2Error(code, errorCode, I18nUtil.message(i18code, args), uri));
    }

}

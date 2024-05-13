package com.thingslink.common.security.oauth2.exception;

import com.thingslink.common.core.utils.MessageUtil;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 自定义OAuth2国际化异常
 * @date : 2024/4/1 14:48:50
 */
public class OAuth2AuthenticationI18nException extends OAuth2AuthenticationException {

    public OAuth2AuthenticationI18nException(String errorCode, String code, Object... args) {
        super(new OAuth2Error(errorCode, MessageUtil.message(code, args), null));
    }

}

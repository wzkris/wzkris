package com.wzkris.common.security.oauth2.utils;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.utils.I18nUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.oauth2.domain.CustomOAuth2Error;
import com.wzkris.common.security.oauth2.exception.OAuth2AuthenticationI18nException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.resource.BearerTokenError;

/**
 * OAuth2异常工具类
 */
public final class OAuth2ExceptionUtil {

    private static final String RESPONSE_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    /**
     * 抛出OAuth2异常
     */
    public static void throwError(int code, String description) {
        throw new OAuth2AuthenticationException(
                new CustomOAuth2Error(code, description)
        );
    }

    public static void throwError(int code, String errorCode, String description) {
        throw new OAuth2AuthenticationException(
                new CustomOAuth2Error(code, errorCode, description, RESPONSE_ERROR_URI)
        );
    }

    /**
     * 抛出OAuth2国际化异常
     */
    public static void throwErrorI18n(int code, String i18code, Object... args) {
        throw new OAuth2AuthenticationI18nException(code, i18code, args);
    }

    public static void throwErrorI18n(int code, String errorCode, String i18code, Object... args) {
        throw new OAuth2AuthenticationI18nException(code, errorCode, RESPONSE_ERROR_URI, i18code, args);
    }

    /**
     * 将OAuth2异常翻译成通用返回值
     */
    public static Result<?> translate(OAuth2Error oAuth2Error) {
        // Bearer Token异常
        if (oAuth2Error instanceof BearerTokenError bearerTokenError) {
            return Result.resp(bearerTokenError.getHttpStatus().value(), bearerTokenError.getErrorCode(), bearerTokenError.getDescription());
        }

        // 自定义OAuth2异常
        if (oAuth2Error instanceof CustomOAuth2Error customOAuth2Error) {
            return Result.resp(customOAuth2Error.getCode(), customOAuth2Error.getErrorCode(), customOAuth2Error.getDescription());
        }

        String errorCode = oAuth2Error.getErrorCode();
        String errorMsg = oAuth2Error.getDescription();

        // OAuth2异常
        if (errorCode.equals(OAuth2ErrorCodes.SERVER_ERROR)) {
            return Result.resp(BizCode.INTERNAL_ERROR, errorMsg);
        } else if (errorCode.equals(OAuth2ErrorCodes.ACCESS_DENIED) || errorCode.equals(OAuth2ErrorCodes.INSUFFICIENT_SCOPE)) {
            return Result.resp(BizCode.FORBID, errorMsg);
        } else if (errorCode.startsWith("unsupported_")) {
            return switch (errorCode) {
                case OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE ->
                        Result.resp(BizCode.BAD_REQUEST, I18nUtil.message("oauth2.unsupport.granttype"));
                case OAuth2ErrorCodes.UNSUPPORTED_TOKEN_TYPE ->
                        Result.resp(BizCode.BAD_REQUEST, I18nUtil.message("oauth2.unsupport.tokentype"));
                case OAuth2ErrorCodes.UNSUPPORTED_RESPONSE_TYPE ->
                        Result.resp(BizCode.BAD_REQUEST, I18nUtil.message("oauth2.unsupport.responsetype"));
                default -> Result.resp(BizCode.BAD_REQUEST, errorMsg);
            };
        } else if (errorCode.equals(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT)) {
            return Result.resp(BizCode.BAD_REQUEST, StringUtil.nullToDefault(errorMsg, I18nUtil.message("oauth2.unsupport.granttype")));
        } else if (errorCode.startsWith("invalid_")) {
            return switch (errorCode) {
                case OAuth2ErrorCodes.INVALID_TOKEN -> // token不合法则返回子状态
                        Result.resp(BizCode.UNAUTHORIZED);
                case OAuth2ErrorCodes.INVALID_GRANT -> // refresh_token刷新失败
                        Result.resp(BizCode.UNAUTHORIZED, StringUtil.nullToDefault(errorMsg, BizCode.UNAUTHORIZED.desc()));
                case OAuth2ErrorCodes.INVALID_SCOPE -> // scope不合法
                        Result.resp(BizCode.BAD_REQUEST, I18nUtil.message("oauth2.scope.invalid"));
                default -> Result.resp(BizCode.BAD_REQUEST, errorMsg);
            };
        } else if (errorCode.equals(OAuth2ErrorCodes.TEMPORARILY_UNAVAILABLE)) {
            return Result.resp(BizCode.BAD_GATEWAY, errorMsg);
        } else {
            return Result.resp(BizCode.UNAUTHORIZED, errorMsg);
        }
    }
}

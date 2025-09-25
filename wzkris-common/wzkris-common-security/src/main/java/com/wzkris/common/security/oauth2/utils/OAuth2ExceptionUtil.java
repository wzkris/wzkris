package com.wzkris.common.security.oauth2.utils;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.common.core.utils.I18nUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.exception.CustomOAuth2Error;
import com.wzkris.common.security.exception.OAuth2AuthenticationI18nException;
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
        throw new OAuth2AuthenticationException(new CustomOAuth2Error(code, description));
    }

    public static void throwError(int code, String errorCode, String description) {
        throw new OAuth2AuthenticationException(
                new CustomOAuth2Error(code, errorCode, description, RESPONSE_ERROR_URI));
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
            return Result.resp(
                    bearerTokenError.getHttpStatus().value(),
                    bearerTokenError.getErrorCode(),
                    bearerTokenError.getDescription());
        }

        // 自定义OAuth2异常
        if (oAuth2Error instanceof CustomOAuth2Error customOAuth2Error) {
            return Result.resp(
                    customOAuth2Error.getCode(), customOAuth2Error.getErrorCode(), customOAuth2Error.getDescription());
        }

        String errorCode = oAuth2Error.getErrorCode();
        String errorMsg = oAuth2Error.getDescription();

        // OAuth2异常
        if (errorCode.equals(OAuth2ErrorCodes.SERVER_ERROR)) {
            return Result.err50000(I18nUtil.message("service.internalError.error"));
        } else if (errorCode.equals(OAuth2ErrorCodes.ACCESS_DENIED)
                || errorCode.equals(OAuth2ErrorCodes.INSUFFICIENT_SCOPE)) {
            return Result.err40003(I18nUtil.message("forbidden.accessDenied.permissionDenied"));
        } else if (errorCode.startsWith("unsupported_")) {
            return switch (errorCode) {
                case OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE ->
                        Result.err40000(I18nUtil.message("oauth2.unsupport.granttype"));
                case OAuth2ErrorCodes.UNSUPPORTED_TOKEN_TYPE ->
                        Result.err40000(I18nUtil.message("oauth2.unsupport.tokentype"));
                case OAuth2ErrorCodes.UNSUPPORTED_RESPONSE_TYPE ->
                        Result.err40000(I18nUtil.message("oauth2.unsupport.responsetype"));
                default -> Result.err40000(errorMsg);
            };
        } else if (errorCode.equals(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT)) {
            return Result.err40000(
                    StringUtil.defaultIfBlank(errorMsg, I18nUtil.message("oauth2.unsupport.granttype")));
        } else if (errorCode.startsWith("invalid_")) {
            return switch (errorCode) {
                case OAuth2ErrorCodes.INVALID_TOKEN -> // token不合法
                        Result.err40001(I18nUtil.message("forbidden.accessDenied.tokenExpired"));
                case OAuth2ErrorCodes.INVALID_GRANT -> // refresh_token刷新失败
                        Result.err40001(StringUtil.defaultIfBlank(errorMsg, I18nUtil.message("forbidden.accessDenied.tokenExpired")));
                case OAuth2ErrorCodes.INVALID_SCOPE -> // scope不合法
                        Result.err40000(I18nUtil.message("oauth2.scope.invalid"));
                case OAuth2ErrorCodes.INVALID_CLIENT -> // 客户端不合法
                        Result.err40000(I18nUtil.message("oauth2.client.invalid"));
                default -> Result.err40000(errorMsg);
            };
        } else if (errorCode.equals(OAuth2ErrorCodes.TEMPORARILY_UNAVAILABLE)) {
            return Result.resp(BizBaseCode.BAD_GATEWAY, errorMsg);
        } else {
            return Result.err40001(errorMsg);
        }
    }

}

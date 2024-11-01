package com.wzkris.common.security.oauth2.utils;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.utils.MessageUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.oauth2.constants.CustomErrorCodes;
import com.wzkris.common.security.oauth2.exception.OAuth2AuthenticationI18nException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.resource.BearerTokenError;

/**
 * OAuth2异常工具类
 */
public final class OAuth2ExceptionUtil {

    private static final String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    /**
     * 抛出OAuth2异常
     */
    public static void throwError(String errorCode, String description) {
        throw new OAuth2AuthenticationException(
                new OAuth2Error(errorCode, description, ACCESS_TOKEN_REQUEST_ERROR_URI)
        );
    }

    /**
     * 抛出OAuth2国际化异常
     */
    public static void throwErrorI18n(String errorCode, String code, Object... args) {
        throw new OAuth2AuthenticationI18nException(errorCode, code, args);
    }

    /**
     * 将OAuth2异常翻译成通用返回值
     */
    public static Result<Void> translate(OAuth2Error oAuth2Error) {
        // Bearer Token异常
        if (oAuth2Error instanceof BearerTokenError bearerTokenError) {
            return Result.resp(bearerTokenError.getHttpStatus().value(), null, bearerTokenError.getDescription());
        }

        String errorCode = oAuth2Error.getErrorCode();
        String errorMsg = oAuth2Error.getDescription();

        // 自定义异常
        if (errorCode.equals(CustomErrorCodes.VALIDATE_ERROR)) {
            return Result.resp(BizCode.PRECONDITION_FAILED, errorMsg);
        }
        else if (errorCode.equals(CustomErrorCodes.FREQUENT_RETRY)) {
            return Result.resp(BizCode.BAD_REQUEST, errorMsg);
        }
        else if (errorCode.equals(CustomErrorCodes.NOT_FOUND)) {
            return Result.resp(BizCode.NOT_FOUND, errorMsg);
        }

        // OAuth2异常
        if (errorCode.equals(OAuth2ErrorCodes.SERVER_ERROR)) {
            return Result.resp(BizCode.INTERNAL_ERROR, errorMsg);
        }
        else if (errorCode.equals(OAuth2ErrorCodes.ACCESS_DENIED) || errorCode.equals(OAuth2ErrorCodes.INSUFFICIENT_SCOPE)) {
            return Result.resp(BizCode.FORBID, errorMsg);
        }
        else if (errorCode.startsWith("unsupported_")) {
            return switch (errorCode) {
                case OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE ->
                        Result.resp(BizCode.BAD_REQUEST, MessageUtil.message("oauth2.unsupport.granttype"));
                case OAuth2ErrorCodes.UNSUPPORTED_TOKEN_TYPE ->
                        Result.resp(BizCode.BAD_REQUEST, MessageUtil.message("oauth2.unsupport.tokentype"));
                case OAuth2ErrorCodes.UNSUPPORTED_RESPONSE_TYPE ->
                        Result.resp(BizCode.BAD_REQUEST, MessageUtil.message("oauth2.unsupport.responsetype"));
                default -> Result.resp(BizCode.BAD_REQUEST, errorMsg);
            };
        }
        else if (errorCode.equals(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT)) {
            return Result.resp(BizCode.BAD_REQUEST, StringUtil.nullToDefault(errorMsg, MessageUtil.message("oauth2.unsupport.granttype")));
        }
        else if (errorCode.startsWith("invalid_")) {
            return switch (errorCode) {
                case OAuth2ErrorCodes.INVALID_TOKEN -> // token不合法则返回子状态
                        Result.resp(BizCode.UNAUTHORIZED);
                case OAuth2ErrorCodes.INVALID_GRANT -> // refresh_token刷新失败
                        Result.resp(BizCode.UNAUTHORIZED, StringUtil.nullToDefault(errorMsg, BizCode.UNAUTHORIZED.desc()));
                case OAuth2ErrorCodes.INVALID_SCOPE -> // scope不合法
                        Result.resp(BizCode.BAD_REQUEST, StringUtil.nullToDefault(errorMsg, MessageUtil.message("oauth2.scope.invalid")));
                default -> Result.resp(BizCode.BAD_REQUEST, errorMsg);
            };
        }
        else if (errorCode.equals(OAuth2ErrorCodes.TEMPORARILY_UNAVAILABLE)) {
            return Result.resp(BizCode.BAD_GATEWAY, errorMsg);
        }
        else {
            return Result.resp(BizCode.UNAUTHORIZED, errorMsg);
        }
    }
}

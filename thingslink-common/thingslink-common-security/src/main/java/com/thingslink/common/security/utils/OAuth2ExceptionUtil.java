package com.thingslink.common.security.utils;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.enums.BizCode;
import com.thingslink.common.core.utils.MessageUtil;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;

/**
 * OAuth2异常工具类
 */
public class OAuth2ExceptionUtil {

    /**
     * 将OAuth2异常翻译成通用返回值
     */
    public static Result<?> translate(OAuth2Error oAuth2Error) {
        String errorCode = oAuth2Error.getErrorCode();
        String errorMsg = oAuth2Error.getDescription();

        // OAuth2异常
        if (errorCode.equals(OAuth2ErrorCodes.SERVER_ERROR)) {
            return Result.resp(BizCode.INTERNAL_ERROR, errorMsg);
        }
        else if (errorCode.equals(OAuth2ErrorCodes.ACCESS_DENIED) || errorCode.equals(OAuth2ErrorCodes.INSUFFICIENT_SCOPE)) {
            return Result.resp(BizCode.FORBID, errorMsg);
        }
        else if (errorCode.equals(OAuth2ErrorCodes.INVALID_TOKEN)) { // token不合法则返回子状态
            return Result.resp(BizCode.UNAUTHORIZED__INVALID_TOKEN);
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
        else if (errorCode.startsWith("invalid_")) {
            return Result.resp(BizCode.BAD_REQUEST, errorMsg);
        }
        else if (errorCode.equals(OAuth2ErrorCodes.TEMPORARILY_UNAVAILABLE)) {
            return Result.resp(BizCode.BAD_GATEWAY, errorMsg);
        }
        else {
            return Result.resp(BizCode.UNAUTHORIZED, errorMsg);
        }
    }
}
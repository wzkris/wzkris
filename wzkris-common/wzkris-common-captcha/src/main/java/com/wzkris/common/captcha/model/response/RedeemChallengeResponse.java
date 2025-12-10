package com.wzkris.common.captcha.model.response;

import com.wzkris.common.captcha.model.Token;

import java.util.Date;

/**
 * token 响应
 *
 * @author wuhunyu
 * @date 2025/06/16 18:34
 **/
public record RedeemChallengeResponse(boolean success, String message, String token, Date expires) {

    public static RedeemChallengeResponse error(String message) {
        return new RedeemChallengeResponse(false, message, null, null);
    }

    public static RedeemChallengeResponse ok(Token token) {
        return new RedeemChallengeResponse(true, null, token.token(), token.expires());
    }

}

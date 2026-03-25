package com.wzkris.captcha.response;

import java.util.Date;

public record RedeemChallengeResponse(boolean success, String message, String token, Date expires) {

    public static RedeemChallengeResponse error(String message) {
        return new RedeemChallengeResponse(false, message, null, null);
    }

    public static RedeemChallengeResponse ok(String token, Date expires) {
        return new RedeemChallengeResponse(true, null, token, expires);
    }

}

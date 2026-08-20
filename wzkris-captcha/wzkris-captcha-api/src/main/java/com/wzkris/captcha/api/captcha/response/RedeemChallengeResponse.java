package com.wzkris.captcha.api.captcha.response;

import java.time.OffsetDateTime;

public record RedeemChallengeResponse(boolean success, String message, String token, OffsetDateTime expires) {

    public static RedeemChallengeResponse error(String message) {
        return new RedeemChallengeResponse(false, message, null, null);
    }

    public static RedeemChallengeResponse ok(String token, OffsetDateTime expires) {
        return new RedeemChallengeResponse(true, null, token, expires);
    }

}

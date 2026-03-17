package com.wzkris.captcha.challenge.domain;

import java.util.Date;

public record RedeemChallengeResult(boolean success, String message, String token, Date expires) {

    public static RedeemChallengeResult error(String message) {
        return new RedeemChallengeResult(false, message, null, null);
    }

    public static RedeemChallengeResult ok(String token, Date expires) {
        return new RedeemChallengeResult(true, null, token, expires);
    }

}

package com.wzkris.risk.domain.resp;

import java.util.Date;

public record RedeemChallengeResp(boolean success, String message, String token, Date expires) {

    public static RedeemChallengeResp error(String message) {
        return new RedeemChallengeResp(false, message, null, null);
    }

    public static RedeemChallengeResp ok(String token, Date expires) {
        return new RedeemChallengeResp(true, null, token, expires);
    }

}

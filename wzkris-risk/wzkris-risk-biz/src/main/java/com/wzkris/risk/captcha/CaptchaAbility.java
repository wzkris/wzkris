package com.wzkris.risk.captcha;

import com.wzkris.risk.domain.dto.ChallengeData;
import com.wzkris.risk.domain.req.RedeemChallengeReq;
import com.wzkris.risk.domain.resp.RedeemChallengeResp;

/**
 * 验证码能力扩展点，用于后续接入图形/滑块/行为等方式。
 */
public interface CaptchaAbility {

    String type();

    ChallengeData createChallenge();

    RedeemChallengeResp redeem(RedeemChallengeReq request);

    Boolean validateToken(String token);

}

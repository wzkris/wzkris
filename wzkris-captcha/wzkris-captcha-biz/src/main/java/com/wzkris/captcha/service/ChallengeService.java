package com.wzkris.captcha.service;

import com.wzkris.captcha.domain.ChallengeCaptchaInfo;
import com.wzkris.captcha.response.RedeemChallengeResponse;

import java.util.List;

public interface ChallengeService {

    ChallengeCaptchaInfo createChallenge();

    RedeemChallengeResponse redeem(String token, List<Integer> solutions);

    Boolean validateToken(String tokenStr);

}

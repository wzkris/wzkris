package com.wzkris.captcha.challenge.service;

import com.wzkris.captcha.challenge.domain.ChallengeData;
import com.wzkris.captcha.challenge.domain.RedeemChallengeResult;

import java.util.List;

public interface ChallengeService {

    ChallengeData createChallenge();

    RedeemChallengeResult redeem(String token, List<Integer> solutions);

    Boolean validateToken(String tokenStr);

}

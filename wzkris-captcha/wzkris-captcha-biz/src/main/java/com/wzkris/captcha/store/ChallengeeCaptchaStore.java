package com.wzkris.captcha.store;

import com.wzkris.captcha.domain.ChallengeCaptchaInfo;

import java.util.Date;

public interface ChallengeeCaptchaStore {

    void putChallenge(String token, ChallengeCaptchaInfo challengeCaptchaInfo);

    ChallengeCaptchaInfo removeChallenge(String token);

    void putToken(String token, Date expires);

    Date removeToken(String token);

}

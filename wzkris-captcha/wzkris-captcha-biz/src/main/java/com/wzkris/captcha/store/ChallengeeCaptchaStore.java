package com.wzkris.captcha.store;

import com.wzkris.captcha.domain.ChallengeCaptchaInfo;

import java.time.OffsetDateTime;

public interface ChallengeeCaptchaStore {

    void putChallenge(String token, ChallengeCaptchaInfo challengeCaptchaInfo);

    ChallengeCaptchaInfo removeChallenge(String token);

    void putToken(String token, OffsetDateTime expires);

    OffsetDateTime removeToken(String token);

}

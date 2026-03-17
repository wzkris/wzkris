package com.wzkris.captcha.challenge.store;

import com.wzkris.captcha.challenge.domain.ChallengeData;

import java.util.Date;

public interface ChallengeStore {

    void putChallenge(String token, ChallengeData challengeData);

    ChallengeData removeChallenge(String token);

    void putToken(String token, Date expires);

    Date removeToken(String token);

}

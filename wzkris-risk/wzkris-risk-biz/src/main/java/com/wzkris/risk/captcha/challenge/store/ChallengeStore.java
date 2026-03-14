package com.wzkris.risk.captcha.challenge.store;

import com.wzkris.risk.domain.dto.ChallengeData;

import java.util.Date;

public interface ChallengeStore {

    void putChallenge(String token, ChallengeData challengeData);

    ChallengeData removeChallenge(String token);

    ChallengeData getChallenge(String token);

    void putToken(String token, Date expires);

    Date removeToken(String token);

    Date getToken(String token);

    void clean();

}

package com.wzkris.risk.captcha.challenge.store.impl;

import com.wzkris.risk.captcha.challenge.store.ChallengeStore;
import com.wzkris.risk.domain.dto.ChallengeData;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存存储
 */
@RequiredArgsConstructor
public class DefaultChallengeStore implements ChallengeStore {

    private final Map<String, ChallengeData> challengeMap = new ConcurrentHashMap<>();

    private final Map<String, Date> tokenMap = new ConcurrentHashMap<>();

    @Override
    public void putChallenge(@NonNull final String token, @NonNull final ChallengeData challengeData) {
        this.cleanChallenge(new Date());

        challengeMap.put(token, challengeData);
    }

    @Override
    public ChallengeData removeChallenge(@NonNull final String token) {
        this.cleanChallenge(new Date());

        return challengeMap.remove(token);
    }

    @Override
    public ChallengeData getChallenge(@NonNull final String token) {
        return challengeMap.get(token);
    }

    @Override
    public void putToken(@NonNull final String token, @NonNull final Date expires) {
        this.cleanToken(new Date());

        tokenMap.put(token, expires);
    }

    @Override
    public Date removeToken(@NonNull final String token) {
        this.cleanToken(new Date());

        return tokenMap.remove(token);
    }

    @Override
    public Date getToken(@NonNull final String token) {
        return tokenMap.get(token);
    }

    @Override
    public void clean() {
        // 获取当前日期时间
        final Date now = new Date();

        this.cleanChallenge(now);
        this.cleanToken(now);
    }

    private void cleanChallenge(Date now) {
        final var challengeIterator = challengeMap.entrySet()
                .iterator();
        while (challengeIterator.hasNext()) {
            final var challenge = challengeIterator.next()
                    .getValue();
            if (challenge.getExpires().before(now)) {
                challengeIterator.remove();
            }
        }
    }

    private void cleanToken(Date now) {
        final var tokenIterator = tokenMap.entrySet()
                .iterator();
        while (tokenIterator.hasNext()) {
            final Date expires = tokenIterator.next()
                    .getValue();
            if (expires.before(now)) {
                tokenIterator.remove();
            }
        }
    }

}

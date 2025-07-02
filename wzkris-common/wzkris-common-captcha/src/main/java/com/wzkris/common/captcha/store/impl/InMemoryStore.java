package com.wzkris.common.captcha.store.impl;

import com.wzkris.common.captcha.model.Challenge;
import com.wzkris.common.captcha.properties.StoreType;
import com.wzkris.common.captcha.store.CapStore;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存存储
 *
 * @author wuhunyu
 * @date 2025/06/16 16:11
 **/
@RequiredArgsConstructor
public class InMemoryStore implements CapStore {

    private final Map<String, Challenge> challengeMap = new ConcurrentHashMap<>();

    private final Map<String, Date> tokenMap = new ConcurrentHashMap<>();

    @Override
    public StoreType storeType() {
        return StoreType.IN_MEMORY;
    }

    @Override
    public boolean putChallenge(@NonNull final String token, @NonNull final Challenge challenge) {
        this.cleanChallenge(new Date());

        challengeMap.put(token, challenge);
        return true;
    }

    @Override
    public Challenge removeChallenge(@NonNull final String token) {
        this.cleanChallenge(new Date());

        return challengeMap.remove(token);
    }

    @Override
    public Challenge getChallenge(@NonNull final String token) {
        return challengeMap.get(token);
    }

    @Override
    public boolean putToken(@NonNull final String token, @NonNull final Date expires) {
        this.cleanToken(new Date());

        tokenMap.put(token, expires);
        return true;
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

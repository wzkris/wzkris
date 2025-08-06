package com.wzkris.common.captcha.store.impl;

import com.wzkris.common.captcha.model.ChallengeData;
import com.wzkris.common.captcha.properties.CapProperties;
import com.wzkris.common.captcha.properties.StoreType;
import com.wzkris.common.captcha.store.CapStore;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.Date;

/**
 * redis 存储
 *
 * @author wuhunyu
 * @date 2025/06/16 23:07
 **/
@RequiredArgsConstructor
public class RedisStore implements CapStore {

    private final RedissonClient redissonClient;

    private final CapProperties capProperties;

    @Override
    public StoreType storeType() {
        return StoreType.REDIS;
    }

    @Override
    public boolean putChallenge(final String token, final ChallengeData challengeData) {
        redissonClient.getBucket(
                        this.makeupChallengeKey(token)
                )
                .set(
                        challengeData,
                        Duration.ofMillis(capProperties.getChallengeExpiresMs())
                );
        return true;
    }

    @Override
    public ChallengeData removeChallenge(final String token) {
        return (ChallengeData) redissonClient.getBucket(
                        this.makeupChallengeKey(token)
                )
                .getAndDelete();
    }

    @Override
    public ChallengeData getChallenge(final String token) {
        return (ChallengeData) redissonClient.getBucket(
                        this.makeupChallengeKey(token)
                )
                .get();
    }

    @Override
    public boolean putToken(final String token, final Date expires) {
        redissonClient.getBucket(
                        this.makeupTokenKey(token)
                )
                .set(
                        expires,
                        Duration.ofMillis(capProperties.getTokenExpiresMs())
                );
        return true;
    }

    @Override
    public Date removeToken(final String token) {
        return (Date) redissonClient.getBucket(
                        this.makeupTokenKey(token)
                )
                .getAndDelete();
    }

    @Override
    public Date getToken(final String token) {
        return (Date) redissonClient.getBucket(
                        this.makeupTokenKey(token)
                )
                .get();
    }

    @Override
    public void clean() {
    }

    private String makeupChallengeKey(final String token) {
        return capProperties.getCaptchaPrefix() + token;
    }

    private String makeupTokenKey(final String token) {
        return capProperties.getCaptchaPrefix() + token;
    }

}

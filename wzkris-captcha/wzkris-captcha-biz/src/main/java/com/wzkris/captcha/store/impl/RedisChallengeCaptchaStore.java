package com.wzkris.captcha.store.impl;

import com.wzkris.captcha.domain.ChallengeCaptchaInfo;
import com.wzkris.captcha.properties.ChallengeCaptchaProperties;
import com.wzkris.captcha.store.ChallengeCaptchaStore;
import com.wzkris.common.redis.util.RedisJsonUtil;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisChallengeCaptchaStore extends AbstractRedisCaptchaStore implements ChallengeCaptchaStore {

    private final ChallengeCaptchaProperties captchaProperties;

    public RedisChallengeCaptchaStore(RedisTemplate<String, Object> redisTemplate, ChallengeCaptchaProperties captchaProperties) {
        super(redisTemplate);
        this.captchaProperties = captchaProperties;
    }

    @Override
    protected String getTokenPrefix() {
        return captchaProperties.getTokenPrefix();
    }

    @Override
    protected long getTokenExpiresMs() {
        return captchaProperties.getTokenExpiresMs();
    }

    @Override
    public void putChallenge(String token, ChallengeCaptchaInfo challengeCaptchaInfo) {
        redisTemplate.opsForValue().set(
                captchaProperties.getChallengePrefix() + token,
                challengeCaptchaInfo,
                captchaProperties.getChallengeExpiresMs(),
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public ChallengeCaptchaInfo removeChallenge(String token) {
        return RedisJsonUtil.parse(redisTemplate.opsForValue().getAndDelete(captchaProperties.getChallengePrefix() + token), ChallengeCaptchaInfo.class);
    }

}
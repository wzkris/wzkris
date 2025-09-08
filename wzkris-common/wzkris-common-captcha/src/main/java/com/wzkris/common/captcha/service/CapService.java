package com.wzkris.common.captcha.service;

import com.wzkris.common.captcha.exception.ChallengeStoreException;
import com.wzkris.common.captcha.handler.CapHandler;
import com.wzkris.common.captcha.model.ChallengeData;
import com.wzkris.common.captcha.model.Token;
import com.wzkris.common.captcha.properties.CapProperties;
import com.wzkris.common.captcha.request.RedeemChallengeRequest;
import com.wzkris.common.captcha.response.RedeemChallengeResponse;
import com.wzkris.common.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

/**
 * 验证码服务
 *
 * @author wzkris
 */
@Slf4j
public class CapService {

    private final CapHandler capHandler;

    private final CapProperties capProperties;

    public CapService(CapHandler capHandler, CapProperties capProperties) {
        this.capHandler = capHandler;
        this.capProperties = capProperties;
    }

    public ChallengeData createChallenge() throws ChallengeStoreException {
        return capHandler.createChallenge();
    }

    public RedeemChallengeResponse redeemChallenge(final RedeemChallengeRequest redeemChallengeRequest) {
        try {
            final Token token = capHandler.redeemChallenge(
                    redeemChallengeRequest.getToken(),
                    redeemChallengeRequest.getSolutions()
            );
            return RedeemChallengeResponse.ok(token);
        } catch (IllegalArgumentException | IllegalStateException | ChallengeStoreException e) {
            return RedeemChallengeResponse.error(e.getMessage());
        }
    }

    public Boolean validateChallenge(final String token) {
        return capHandler.validateToken(token);
    }

    /**
     * 设置验证码
     *
     * @param key 前缀
     */
    public void setCaptcha(String key, String code) {
        RedisUtil.setObj(
                capProperties.getCaptchaPrefix() + key,
                code,
                Duration.ofMillis(capProperties.getTokenExpiresMs()));
    }

}

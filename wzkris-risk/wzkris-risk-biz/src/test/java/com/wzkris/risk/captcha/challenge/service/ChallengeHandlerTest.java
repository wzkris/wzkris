package com.wzkris.risk.captcha.challenge.service;

import com.wzkris.risk.captcha.CaptchaAbilityRegistry;
import com.wzkris.risk.captcha.challenge.properties.ChallengeCaptchaProperties;
import com.wzkris.risk.captcha.challenge.store.impl.DefaultChallengeStore;
import com.wzkris.risk.domain.dto.ChallengeData;
import com.wzkris.risk.domain.req.RedeemChallengeReq;
import com.wzkris.risk.domain.resp.RedeemChallengeResp;
import com.wzkris.risk.service.captcha.CaptchaService;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ChallengeHandlerTest {

    @Test
    void createChallengeAndRedeem_shouldSucceed() {
        ChallengeCaptchaProperties properties = testProperties();
        ChallengeHandler handler = new ChallengeHandler(properties, new DefaultChallengeStore());
        CaptchaService service = new CaptchaService(
                new CaptchaAbilityRegistry(List.of(handler)),
                properties,
                mock(StringRedisTemplate.class));

        ChallengeData challenge = service.createChallenge();
        List<Integer> solutions = solve(challenge.getToken(), properties);
        RedeemChallengeReq request = RedeemChallengeReq.builder()
                .token(challenge.getToken())
                .solutions(solutions)
                .build();

        RedeemChallengeResp response = service.redeem(request);
        assertTrue(response.success());
        assertNotNull(response.token());
        assertNotNull(response.expires());
    }

    @Test
    void redeem_shouldFailWhenSolutionInvalid() {
        ChallengeCaptchaProperties properties = testProperties();
        ChallengeHandler handler = new ChallengeHandler(properties, new DefaultChallengeStore());
        CaptchaService service = new CaptchaService(
                new CaptchaAbilityRegistry(List.of(handler)),
                properties,
                mock(StringRedisTemplate.class));

        ChallengeData challenge = service.createChallenge();
        RedeemChallengeReq request = RedeemChallengeReq.builder()
                .token(challenge.getToken())
                .solutions(List.of(1, 2))
                .build();

        RedeemChallengeResp response = service.redeem(request);
        assertFalse(response.success());
    }

    @Test
    void redeem_shouldFailWhenSolutionCountMismatch() {
        ChallengeCaptchaProperties properties = testProperties();
        properties.setChallengeCount(3);
        ChallengeHandler handler = new ChallengeHandler(properties, new DefaultChallengeStore());
        CaptchaService service = new CaptchaService(
                new CaptchaAbilityRegistry(List.of(handler)),
                properties,
                mock(StringRedisTemplate.class));

        ChallengeData challenge = service.createChallenge();
        RedeemChallengeResp response = service.redeem(RedeemChallengeReq.builder()
                .token(challenge.getToken())
                .solutions(List.of(1, 2))
                .build());
        assertFalse(response.success());
    }

    @Test
    void validateToken_shouldBeOneTime() {
        ChallengeCaptchaProperties properties = testProperties();
        ChallengeHandler handler = new ChallengeHandler(properties, new DefaultChallengeStore());
        CaptchaService service = new CaptchaService(
                new CaptchaAbilityRegistry(List.of(handler)),
                properties,
                mock(StringRedisTemplate.class));

        ChallengeData challenge = service.createChallenge();
        RedeemChallengeResp redeem = service.redeem(RedeemChallengeReq.builder()
                .token(challenge.getToken())
                .solutions(solve(challenge.getToken(), properties))
                .build());
        assertTrue(redeem.success());

        assertTrue(service.validateChallenge(redeem.token()));
        assertFalse(service.validateChallenge(redeem.token()));
    }

    @Test
    void redeem_shouldFailWhenChallengeExpired() throws InterruptedException {
        ChallengeCaptchaProperties properties = testProperties();
        properties.setChallengeExpiresMs(10L);
        ChallengeHandler handler = new ChallengeHandler(properties, new DefaultChallengeStore());
        CaptchaService service = new CaptchaService(
                new CaptchaAbilityRegistry(List.of(handler)),
                properties,
                mock(StringRedisTemplate.class));

        ChallengeData challenge = service.createChallenge();
        Thread.sleep(20L);

        RedeemChallengeResp response = service.redeem(RedeemChallengeReq.builder()
                .token(challenge.getToken())
                .solutions(solve(challenge.getToken(), properties))
                .build());
        assertFalse(response.success());
    }

    private ChallengeCaptchaProperties testProperties() {
        ChallengeCaptchaProperties properties = new ChallengeCaptchaProperties();
        properties.setChallengeCount(2);
        properties.setChallengeLength(8);
        properties.setChallengeDifficulty(1);
        properties.setChallengeExpiresMs(5_000L);
        properties.setTokenExpiresMs(5_000L);
        properties.setIdSize(8);
        return properties;
    }

    private List<Integer> solve(String token, ChallengeCaptchaProperties properties) {
        List<Integer> solutions = new ArrayList<>();
        for (int i = 0; i < properties.getChallengeCount(); i++) {
            String salt = ChallengeHandler.prng("%s%d".formatted(token, i + 1), properties.getChallengeLength());
            String target = ChallengeHandler.prng("%s%dd".formatted(token, i + 1), properties.getChallengeDifficulty());
            int answer = 0;
            while (true) {
                if (DigestUtils.sha256Hex(salt + answer).startsWith(target)) {
                    solutions.add(answer);
                    break;
                }
                answer++;
            }
        }
        return solutions;
    }

}

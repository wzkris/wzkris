package com.wzkris.risk.captcha.challenge.store.impl;

import com.wzkris.risk.captcha.challenge.properties.ChallengeCaptchaProperties;
import com.wzkris.risk.domain.dto.ChallengeData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedisChallengeStoreTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    private RedisChallengeStore store;

    @BeforeEach
    void setUp() {
        ChallengeCaptchaProperties properties = new ChallengeCaptchaProperties();
        properties.setChallengePrefix("captcha-challenge:");
        properties.setTokenPrefix("captcha-token:");
        store = new RedisChallengeStore(redisTemplate, properties);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void putToken_shouldUseTokenPrefixNamespace() {
        Date expires = new Date();

        store.putToken("abc", expires);

        verify(valueOperations).set(eq("captcha-token:abc"), eq(expires), anyLong(), any());
    }

    @Test
    void removeToken_shouldUseAtomicGetAndDelete() {
        Date expires = new Date();
        when(valueOperations.getAndDelete("captcha-token:abc")).thenReturn(expires);

        Date actual = store.removeToken("abc");

        assertNotNull(actual);
        assertEquals(expires, actual);
        verify(valueOperations).getAndDelete("captcha-token:abc");
    }

    @Test
    void removeChallenge_shouldUseAtomicGetAndDelete() {
        ChallengeData challengeData = new ChallengeData();
        when(valueOperations.getAndDelete("captcha-challenge:token")).thenReturn(challengeData);

        ChallengeData actual = store.removeChallenge("token");

        assertEquals(challengeData, actual);
        verify(valueOperations).getAndDelete("captcha-challenge:token");
    }

}

package com.wzkris.captcha.store.impl;

import com.wzkris.captcha.domain.ChallengeCaptchaInfo;
import com.wzkris.captcha.properties.ChallengeCaptchaProperties;
import com.wzkris.captcha.store.ChallengeeCaptchaStore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class RedisChallengeeCaptchaStore implements ChallengeeCaptchaStore {

    private static final DateTimeFormatter LEGACY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RedisTemplate<String, Object> redisTemplate;

    private final ChallengeCaptchaProperties captchaProperties;

    @Override
    public void putChallenge(String token, ChallengeCaptchaInfo challengeCaptchaInfo) {
        redisTemplate.opsForValue().set(
                makeupChallengeKey(token),
                challengeCaptchaInfo,
                captchaProperties.getChallengeExpiresMs(),
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public ChallengeCaptchaInfo removeChallenge(String token) {
        return (ChallengeCaptchaInfo) redisTemplate.opsForValue().getAndDelete(makeupChallengeKey(token));
    }

    @Override
    public void putToken(String token, OffsetDateTime expires) {
        redisTemplate.opsForValue().set(
                makeupTokenKey(token),
                expires,
                captchaProperties.getTokenExpiresMs(),
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public OffsetDateTime removeToken(String token) {
        Object value = redisTemplate.opsForValue().getAndDelete(makeupTokenKey(token));
        return parseOffsetDateTime(value);
    }

    private OffsetDateTime parseOffsetDateTime(Object value) {
        switch (value) {
            case null -> {
                return null;
            }
            case OffsetDateTime offsetDateTime -> {
                return offsetDateTime;
            }
            case String text -> {
                if (text.isBlank()) {
                    return null;
                }
                try {
                    return OffsetDateTime.parse(text);
                } catch (DateTimeParseException ignored) {
                    LocalDateTime localDateTime = LocalDateTime.parse(text, LEGACY_FORMATTER);
                    return localDateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime();
                }
            }
            default -> {
            }
        }
        throw new IllegalStateException("Unsupported token expires value type: " + value.getClass().getName());
    }

    private String makeupChallengeKey(String token) {
        return captchaProperties.getChallengePrefix() + token;
    }

    private String makeupTokenKey(String token) {
        return captchaProperties.getTokenPrefix() + token;
    }

}

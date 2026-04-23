package com.wzkris.captcha.store.impl;

import com.wzkris.captcha.domain.SlideCaptchaInfo;
import com.wzkris.captcha.properties.SlideCaptchaProperties;
import com.wzkris.captcha.store.SlideCaptchaStore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class RedisSlideCaptchaStore implements SlideCaptchaStore {

    private static final DateTimeFormatter LEGACY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RedisTemplate<String, Object> redisTemplate;

    private final SlideCaptchaProperties captchaProperties;

    @Override
    public void putCaptcha(String token, SlideCaptchaInfo captchaInfo) {
        redisTemplate.opsForValue().set(
                makeupCaptchaKey(token),
                captchaInfo,
                captchaProperties.getCaptchaExpiresMs(),
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public SlideCaptchaInfo removeCaptcha(String token) {
        return (SlideCaptchaInfo) redisTemplate.opsForValue().getAndDelete(makeupCaptchaKey(token));
    }

    @Override
    public void putToken(String tokenKey, OffsetDateTime expires) {
        redisTemplate.opsForValue().set(
                makeupTokenKey(tokenKey),
                expires,
                captchaProperties.getTokenExpiresMs(),
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public OffsetDateTime removeToken(String tokenKey) {
        Object value = redisTemplate.opsForValue().getAndDelete(makeupTokenKey(tokenKey));
        return parseOffsetDateTime(value);
    }

    private OffsetDateTime parseOffsetDateTime(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof OffsetDateTime) {
            return (OffsetDateTime) value;
        }
        if (value instanceof String text) {
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
        throw new IllegalStateException("Unsupported token expires value type: " + value.getClass().getName());
    }

    private String makeupCaptchaKey(String token) {
        return captchaProperties.getCaptchaPrefix() + token;
    }

    private String makeupTokenKey(String token) {
        return captchaProperties.getTokenPrefix() + token;
    }

}

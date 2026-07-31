package com.wzkris.captcha.store.impl;

import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.TimeUnit;

/**
 * 验证码存储的 Redis 公共实现，提供 token 存取与过期时间解析能力。
 */
public abstract class AbstractRedisCaptchaStore {

    private static final DateTimeFormatter LEGACY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    protected final RedisTemplate<String, Object> redisTemplate;

    protected AbstractRedisCaptchaStore(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    protected static OffsetDateTime parseOffsetDateTime(Object value) {
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

    protected abstract String getTokenPrefix();

    protected abstract long getTokenExpiresMs();

    public void putToken(String tokenKey, OffsetDateTime expires) {
        redisTemplate.opsForValue().set(
                makeupTokenKey(tokenKey),
                expires,
                getTokenExpiresMs(),
                TimeUnit.MILLISECONDS
        );
    }

    public OffsetDateTime removeToken(String tokenKey) {
        Object value = redisTemplate.opsForValue().getAndDelete(makeupTokenKey(tokenKey));
        return parseOffsetDateTime(value);
    }

    private String makeupTokenKey(String token) {
        return getTokenPrefix() + token;
    }

}
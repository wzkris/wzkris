package com.wzkris.gateway.service;

import com.wzkris.captcha.properties.RiskPassProperties;
import com.wzkris.captcha.risk.RiskClientKeys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 网关风控锁定：写入后 {@link com.wzkris.gateway.filter.RiskCaptchaFilter} 要求持有有效通行票（除放行路径外）。
 */
@Service
@RequiredArgsConstructor
public class RiskLockService {

    private final StringRedisTemplate stringRedisTemplate;

    private final RiskPassProperties riskPassProperties;

    public boolean isLocked(HttpServletRequest request) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(lockRedisKey(request)));
    }

    public void lock(HttpServletRequest request, Duration ttl) {
        stringRedisTemplate.opsForValue().set(lockRedisKey(request), "1", ttl);
    }

    public void unlock(HttpServletRequest request) {
        stringRedisTemplate.delete(lockRedisKey(request));
    }

    private String lockRedisKey(HttpServletRequest request) {
        return riskPassProperties.getLockKeyPrefix() + RiskClientKeys.defaultCompositeKey(request);
    }

}

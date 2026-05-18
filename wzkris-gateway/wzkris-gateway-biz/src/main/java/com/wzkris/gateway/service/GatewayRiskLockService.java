package com.wzkris.gateway.service;

import com.wzkris.gateway.constants.GatewayRiskRedisKeys;
import com.wzkris.gateway.utils.GatewayRiskClientKeys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class GatewayRiskLockService {

    private final StringRedisTemplate stringRedisTemplate;

    public boolean isLocked(HttpServletRequest request) {
        return stringRedisTemplate.hasKey(lockRedisKey(request));
    }

    public void lock(HttpServletRequest request, Duration ttl) {
        stringRedisTemplate.opsForValue().set(lockRedisKey(request), "1", ttl);
    }

    public void unlock(String clientKey) {
        stringRedisTemplate.delete(GatewayRiskRedisKeys.lockKey(clientKey));
    }

    private String lockRedisKey(HttpServletRequest request) {
        return GatewayRiskRedisKeys.lockKey(GatewayRiskClientKeys.defaultCompositeKey(request));
    }

}

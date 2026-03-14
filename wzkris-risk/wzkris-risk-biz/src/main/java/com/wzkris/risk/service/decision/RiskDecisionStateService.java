package com.wzkris.risk.service.decision;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.risk.properties.RiskProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RiskDecisionStateService {

    private static final String PREFIX = "risk:decide:";

    private final StringRedisTemplate redisTemplate;

    private final RiskProperties riskProperties;

    public boolean reachIpLimit(String ipAddr) {
        if (StringUtil.isBlank(ipAddr)) {
            return false;
        }
        String key = PREFIX + "ip:" + ipAddr;
        Long value = redisTemplate.opsForValue().increment(key);
        if (value != null && value == 1L) {
            redisTemplate.expire(key, Duration.ofSeconds(riskProperties.getIpLimitWindowSeconds()));
        }
        return value != null && value > riskProperties.getIpLimitThreshold();
    }

    public boolean reachPathBurst(String requestPath, String ipAddr) {
        if (StringUtil.isAnyBlank(requestPath, ipAddr)) {
            return false;
        }
        String pathKey = PREFIX + "path:" + ipAddr + ":" + requestPath;
        Long value = redisTemplate.opsForValue().increment(pathKey);
        if (value != null && value == 1L) {
            redisTemplate.expire(pathKey, Duration.ofSeconds(riskProperties.getPathBurstWindowSeconds()));
        }
        return value != null && value > riskProperties.getPathBurstThreshold();
    }

    public boolean isIpDrift(String authType, String subject, String ipAddr) {
        if (StringUtil.isAnyBlank(authType, subject, ipAddr)) {
            return false;
        }
        String key = PREFIX + "last_ip:" + authType + ":" + subject;
        String subnet = extractSubnet(ipAddr);
        String previous = redisTemplate.opsForValue().get(key);
        return StringUtil.isNotBlank(previous) && !StringUtil.equals(previous, subnet);
    }

    public boolean isUaMutation(String authType, String subject, String userAgent) {
        if (StringUtil.isAnyBlank(authType, subject, userAgent)) {
            return false;
        }
        String key = PREFIX + "last_ua:" + authType + ":" + subject;
        String previous = redisTemplate.opsForValue().get(key);
        return StringUtil.isNotBlank(previous) && !StringUtil.equals(previous, userAgent);
    }

    public void updateLastIp(String authType, String subject, String ipAddr) {
        if (StringUtil.isAnyBlank(authType, subject, ipAddr)) {
            return;
        }
        String key = PREFIX + "last_ip:" + authType + ":" + subject;
        String subnet = extractSubnet(ipAddr);
        redisTemplate.opsForValue().set(key, subnet, Duration.ofMinutes(riskProperties.getIpDriftWindowMinutes()));
    }

    public void updateLastUa(String authType, String subject, String userAgent) {
        if (StringUtil.isAnyBlank(authType, subject, userAgent)) {
            return;
        }
        String key = PREFIX + "last_ua:" + authType + ":" + subject;
        redisTemplate.opsForValue().set(key, userAgent, Duration.ofMinutes(riskProperties.getUaMutationWindowMinutes()));
    }

    public long getFailCount(String authType, String subject) {
        String value = redisTemplate.opsForValue().get(failKey(authType, subject));
        if (StringUtil.isBlank(value)) {
            return 0L;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ignore) {
            return 0L;
        }
    }

    public void clearFailCount(String authType, String subject) {
        redisTemplate.delete(failKey(authType, subject));
    }

    public void incrementFailCount(String authType, String subject) {
        String key = failKey(authType, subject);
        Long value = redisTemplate.opsForValue().increment(key);
        if (value != null && value == 1L) {
            redisTemplate.expire(key, Duration.ofSeconds(riskProperties.getFailWindowSeconds()));
        }
    }

    private String extractSubnet(String ipAddr) {
        if (!ipAddr.contains(".")) {
            return ipAddr;
        }
        String[] parts = ipAddr.split("\\.");
        if (parts.length < 3) {
            return ipAddr;
        }
        return parts[0] + "." + parts[1] + "." + parts[2];
    }

    private String failKey(String authType, String subject) {
        return PREFIX + "fail:" + authType + ":" + subject;
    }

}

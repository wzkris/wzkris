package com.wzkris.auth.serviceimpl;

import com.wzkris.auth.properties.LoginRiskProperties;
import com.wzkris.common.core.enums.RiskLevelEnum;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 登录风险分析（最小规则版）
 */
@Service
@RequiredArgsConstructor
public class LoginRiskAnalyzeService {

    private static final String RISK_KEY_PREFIX = "auth:risk:login:";

    private static final String TAG_HIGH_FAIL_FREQ = "HIGH_FAIL_FREQ";

    private static final String TAG_IP_DRIFT = "IP_DRIFT";

    private static final String TAG_UA_MUTATION = "UA_MUTATION";

    private static final String TAG_OFF_HOURS_LOGIN = "OFF_HOURS_LOGIN";

    private final StringRedisTemplate redisTemplate;

    private final LoginRiskProperties loginRiskProperties;

    public RiskResult analyze(BaseLoginUser loginUser, String ipAddr, String userAgent, Boolean success, Date loginTime) {
        List<String> abnormalTags = new ArrayList<>();
        int score = 0;
        String userKey = buildUserKey(loginUser);

        // 10分钟内失败次数
        if (Boolean.FALSE.equals(success)) {
            long failCount = addAndGetFailCount(userKey);
            if (failCount >= loginRiskProperties.getHighFailFreqThreshold()) {
                abnormalTags.add(TAG_HIGH_FAIL_FREQ);
                score += 40;
            }
            score += 20;
        }

        // IP网段漂移
        if (StringUtil.isNotBlank(ipAddr)) {
            String currentSubnet = extractSubnet(ipAddr);
            String ipKey = RISK_KEY_PREFIX + "last_ip_subnet:" + userKey;
            String previousSubnet = redisTemplate.opsForValue().get(ipKey);
            if (StringUtil.isNotBlank(previousSubnet) && !StringUtil.equals(previousSubnet, currentSubnet)) {
                abnormalTags.add(TAG_IP_DRIFT);
                score += 30;
            }
            redisTemplate.opsForValue().set(ipKey, currentSubnet, Duration.ofMinutes(loginRiskProperties.getIpDriftWindowMinutes()));
        }

        // UA突变
        if (StringUtil.isNotBlank(userAgent)) {
            String uaKey = RISK_KEY_PREFIX + "last_ua:" + userKey;
            String previousUa = redisTemplate.opsForValue().get(uaKey);
            if (StringUtil.isNotBlank(previousUa) && !StringUtil.equals(previousUa, userAgent)) {
                abnormalTags.add(TAG_UA_MUTATION);
                score += 20;
            }
            redisTemplate.opsForValue().set(uaKey, userAgent, Duration.ofMinutes(loginRiskProperties.getUaMutationWindowMinutes()));
        }

        // 非常用时段成功登录
        LocalDateTime dateTime = LocalDateTime.ofInstant(loginTime.toInstant(), ZoneId.systemDefault());
        int hour = dateTime.getHour();
        int offHoursStart = loginRiskProperties.getOffHoursStart();
        int offHoursEnd = loginRiskProperties.getOffHoursEnd();
        boolean inOffHours = offHoursStart <= offHoursEnd
                ? (hour >= offHoursStart && hour < offHoursEnd)
                : (hour >= offHoursStart || hour < offHoursEnd);
        if (Boolean.TRUE.equals(success) && inOffHours) {
            abnormalTags.add(TAG_OFF_HOURS_LOGIN);
            score += 10;
        }

        score = Math.min(score, 100);
        RiskLevelEnum riskLevel = toRiskLevel(score);
        return new RiskResult(String.join(",", abnormalTags), riskLevel, score);
    }

    public boolean shouldAlert(BaseLoginUser loginUser, RiskResult result) {
        if (!shouldAlert(result)) {
            return false;
        }
        String dedupeKey = RISK_KEY_PREFIX + "alert:" + buildUserKey(loginUser) + ":" + result.riskLevel().getValue();
        Boolean absent = redisTemplate.opsForValue()
                .setIfAbsent(dedupeKey, "1", Duration.ofMinutes(loginRiskProperties.getAlertSuppressMinutes()));
        return Boolean.TRUE.equals(absent);
    }

    public boolean shouldAlert(RiskResult result) {
        if (RiskLevelEnum.HIGH == result.riskLevel()) {
            return true;
        }
        if (RiskLevelEnum.MEDIUM == result.riskLevel()) {
            return StringUtil.contains(result.abnormalTags(), TAG_HIGH_FAIL_FREQ)
                    || StringUtil.contains(result.abnormalTags(), TAG_IP_DRIFT);
        }
        return false;
    }

    private String buildUserKey(BaseLoginUser loginUser) {
        return loginUser.getAuthType().getValue() + ":" + loginUser.getUid();
    }

    private long addAndGetFailCount(String userKey) {
        String key = RISK_KEY_PREFIX + "fail_count:" + userKey;
        Long count = redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, Duration.ofMinutes(loginRiskProperties.getFailWindowMinutes()));
        return count == null ? 0L : count;
    }

    private RiskLevelEnum toRiskLevel(int score) {
        if (score >= 70) {
            return RiskLevelEnum.HIGH;
        }
        if (score >= 40) {
            return RiskLevelEnum.MEDIUM;
        }
        return RiskLevelEnum.LOW;
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

    public record RiskResult(String abnormalTags, RiskLevelEnum riskLevel, Integer riskScore) {

    }

}

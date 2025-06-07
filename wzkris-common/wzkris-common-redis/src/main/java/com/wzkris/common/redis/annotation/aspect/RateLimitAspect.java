package com.wzkris.common.redis.annotation.aspect;

import com.wzkris.common.core.exception.request.TooManyRequestException;
import com.wzkris.common.redis.annotation.RateLimit;
import com.wzkris.common.redis.util.RedisUtil;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.redisson.api.RRateLimiter;

/**
 * 限流处理
 *
 * @author wzkris
 * @date : 2025/02/17 09:01
 */
@Slf4j
@Aspect
public class RateLimitAspect {

    @Before("@annotation(rateLimit)")
    public void doBefore(JoinPoint point, RateLimit rateLimit) {
        // 获取限流器的唯一标识（默认为方法名）
        String key = rateLimit.key().isEmpty() ? point.getSignature().toShortString() : rateLimit.key();
        RRateLimiter rateLimiter = RedisUtil.getClient().getRateLimiter(key);

        // 设置限流规则
        rateLimiter.trySetRate(
                rateLimit.rateType(),
                rateLimit.rate(),
                Duration.ofSeconds(rateLimit.timeWindows()),
                Duration.ofDays(1));

        // 获取许可
        if (!rateLimiter.tryAcquire()) {
            throw new TooManyRequestException();
        }
    }
}

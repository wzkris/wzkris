package com.wzkris.captcha.remote.impl;

import com.wzkris.captcha.remote.api.CaptchaRemoteApi;
import com.wzkris.captcha.remote.api.request.CaptchaCheckRequest;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class CaptchaRemoteApiImpl implements CaptchaRemoteApi {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public Result<Boolean> check(CaptchaCheckRequest request) {
        // 通用失败次数限制：避免对同一 key 暴力猜解
        // 说明：不改变现有接口形态；通过 key 派生 fail/lock key 实现增强。
        final String lockKey = "captcha:lock:" + request.getKey();
        final String failKey = "captcha:fail:" + request.getKey();
        if (stringRedisTemplate.hasKey(lockKey)) {
            return Result.ok(false);
        }

        String value = stringRedisTemplate.opsForValue().get(request.getKey());
        boolean equals = StringUtil.equals(request.getValue(), value);
        if (equals) {
            stringRedisTemplate.delete(Arrays.asList(lockKey, failKey));
        }
        if (!equals) {
            Long failCount = stringRedisTemplate.opsForValue().increment(failKey);
            // 首次失败设置一个窗口期，与验证码 TTL 解耦
            if (failCount != null && failCount == 1L) {
                stringRedisTemplate.expire(failKey, Duration.ofMinutes(10));
            }
            // 连续失败超过阈值，短时间锁定
            if (failCount != null && failCount >= 10L) {
                stringRedisTemplate.opsForValue().set(lockKey, "1", Duration.ofMinutes(5));
            }
        }
        return Result.ok(equals);
    }

}

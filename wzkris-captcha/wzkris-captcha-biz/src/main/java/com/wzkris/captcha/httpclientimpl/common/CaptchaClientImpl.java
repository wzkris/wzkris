package com.wzkris.captcha.httpclientimpl.common;

import com.wzkris.captcha.httpclient.common.CaptchaClient;
import com.wzkris.captcha.httpclient.common.req.CaptchaCheckReq;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Hidden
@Validated
@RestController
@RequiredArgsConstructor
public class CaptchaClientImpl implements CaptchaClient {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public Result<Boolean> check(CaptchaCheckReq captchaCheckReq) {
        String key = captchaCheckReq.getKey();
        String expected = captchaCheckReq.getValue();

        if (StringUtil.isBlank(key) || StringUtil.isBlank(expected)) {
            return Result.ok(false);
        }

        // 通用失败次数限制：避免对同一 key 暴力猜解
        // 说明：不改变现有接口形态；通过 key 派生 fail/lock key 实现增强。
        String lockKey = "captcha:lock:" + key;
        if (stringRedisTemplate.hasKey(lockKey)) {
            return Result.ok(false);
        }

        String value = stringRedisTemplate.opsForValue().get(key);
        boolean equals = StringUtil.equals(captchaCheckReq.getValue(), value);
        if (equals) {
            stringRedisTemplate.delete(key);
            stringRedisTemplate.delete("captcha:fail:" + key);
        }
        if (!equals) {
            String failKey = "captcha:fail:" + key;
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

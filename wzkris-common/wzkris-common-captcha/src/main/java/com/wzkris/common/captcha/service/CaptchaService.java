package com.wzkris.common.captcha.service;

import cloud.tianai.captcha.application.ImageCaptchaProperties;
import cn.hutool.core.util.ObjUtil;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.captcha.CaptchaException;
import com.wzkris.common.core.exception.request.TooManyRequestException;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;

/**
 * 验证码服务
 *
 * @author wzkris
 */
@Slf4j
@Service
public class CaptchaService {

    private static final String LOCK_PREFIX = "captcha:lock:";

    private static final String MAXTRY_PREFIX = "captcha:max_try:";

    @Autowired
    private ImageCaptchaProperties captchaProperties;

    /**
     * 设置验证码
     *
     * @param key 前缀
     */
    public void setCaptcha(String key, String code) {
        RedisUtil.setObj(captchaProperties.getPrefix() + ":" + key, code,
                Duration.ofMillis(captchaProperties.getExpire().getOrDefault("default", 120_000L)));
    }

    /**
     * 校验验证码
     */
    public void validateCaptcha(String key, String code) {
        String fullKey = captchaProperties.getPrefix() + ":" + key;
        String realcode = RedisUtil.getObj(fullKey);
        if (StringUtil.isBlank(realcode)) {
            throw new CaptchaException("captcha.expired");
        }
        if (ObjUtil.notEqual(realcode, code)) {
            throw new CaptchaException("captcha.error");
        }
        RedisUtil.delObj(fullKey);
    }

    /**
     * 验证最大尝试次数
     *
     * @param key     唯一标识
     * @param maxTry  最大尝试次数, 超出则抛出异常
     * @param timeout 超时时长（秒）
     */
    public void validateMaxTry(String key, int maxTry, int timeout) {
        // 构建 Redis 中的键名
        String counterKey = MAXTRY_PREFIX + key;

        // 定义 Lua 脚本
        String luaScript =
                "local currentTry = redis.call('get', KEYS[1]) or 0 " +
                        "if tonumber(currentTry) >= tonumber(ARGV[1]) then " +
                        "    return 0 " +
                        "else " +
                        "    redis.call('incr', KEYS[1]) " +
                        "    redis.call('expire', KEYS[1], ARGV[2]) " +
                        "    return 1 " +
                        "end";

        // 执行 Lua 脚本
        RScript script = RedisUtil.getClient().getScript();
        Long result = script.eval(RScript.Mode.READ_WRITE, luaScript, RScript.ReturnType.INTEGER, Collections.singletonList(counterKey), maxTry, timeout);

        // 检查结果
        if (result.intValue() == 0) {
            throw new TooManyRequestException();
        }
    }

    /**
     * 冻结账号
     *
     * @param key     唯一标识
     * @param timeout 冻结时长（秒）
     */
    public void lockAccount(String key, int timeout) {
        RedisUtil.setObj(LOCK_PREFIX + key, "", timeout);
    }

    /**
     * 校验账号是否被冻结
     */
    public void validateLock(String key) {
        if (RedisUtil.hasKey(LOCK_PREFIX + key)) {
            throw new CaptchaException(BizCode.BAD_REQUEST.value(), "service.busy");
        }
    }
}

package com.wzkris.common.captcha.service;

import cloud.tianai.captcha.application.ImageCaptchaProperties;
import cn.hutool.core.util.ObjUtil;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.captcha.CaptchaException;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 验证码服务
 *
 * @author wzkris
 */
@Slf4j
@Service
public class CaptchaService {

    private static final String ACCOUNT_LOCK_KEY = "account:lock:";

    @Autowired
    private ImageCaptchaProperties captchaProperties;

    /**
     * 设置验证码
     *
     * @param key 前缀
     */
    public void setCaptcha(String key, String code) {
        String fullKey = captchaProperties.getPrefix() + ":" + key;
        if (RedisUtil.hasKey(fullKey)) {
            // 存在则不允许多次调用
            throw new CaptchaException(BizCode.TOO_MANY_REQUESTS.value(), "frequent.retry");
        }
        RedisUtil.setObj(fullKey, code,
                Duration.ofMillis(captchaProperties.getExpire().getOrDefault("default", 120000L)));
    }

    /**
     * 校验验证码
     */
    public void validateCaptcha(String key, String code) {
        String fullKey = captchaProperties.getPrefix() + ":" + key;
        String realcode = RedisUtil.getObj(fullKey);
        if (StringUtil.isBlank(realcode)) {
            throw new CaptchaException(BizCode.PRECONDITION_FAILED.value(), "captcha.expired");
        }
        if (ObjUtil.notEqual(realcode, code)) {
            throw new CaptchaException(BizCode.PRECONDITION_FAILED.value(), "captcha.error");
        }
        RedisUtil.delObj(fullKey);
    }

    /**
     * 冻结账号
     */
    public void lockAccount(String key) {
        RedisUtil.setObj(ACCOUNT_LOCK_KEY + key, "", 600);
    }

    /**
     * 校验账号是否被冻结
     */
    public void validateLock(String key) {
        if (RedisUtil.hasKey(ACCOUNT_LOCK_KEY + key)) {
            throw new CaptchaException(BizCode.BAD_REQUEST.value(), "service.busy");
        }
    }
}

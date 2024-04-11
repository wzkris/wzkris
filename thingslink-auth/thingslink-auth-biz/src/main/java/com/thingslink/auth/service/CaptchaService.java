package com.thingslink.auth.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.google.code.kaptcha.Producer;
import com.thingslink.auth.config.CaptchaConfig;
import com.thingslink.common.core.enums.BizCode;
import com.thingslink.common.core.exception.BusinessException;
import com.thingslink.common.core.exception.BusinessExceptionI18n;
import com.thingslink.common.core.exception.param.CaptchaException;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.redis.util.RedisUtil;
import jakarta.annotation.Resource;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码服务
 *
 * @author wzkris
 */
@Component
public class CaptchaService {
    // 短信验证码前缀 + phoneNumber
    private static final String SMS_CODE_PREFIX = "captcha:sms:";
    // 图片验证码前缀 + uuid
    private static final String PIC_CODE_PREFIX = "captcha:pic:";

    // 最大重试次数
    private static final int maxRetryCount = 5;
    // 账号冻结缓存前缀 + username
    private static final String ACCOUNT_LOCK_PREFIX = "lock:account:";

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private CaptchaConfig captchaConfig;

    /**
     * 生成验证码
     */
    public Map<String, Object> createPicCaptcha() throws IOException {
        Map<String, Object> res = new HashMap<>(2);
        boolean captchaEnabled = captchaConfig.getEnabled();
        res.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled) {
            return res;
        }
        // 保存验证码信息
        String uuid = IdUtil.simpleUUID();

        String capStr, code = null;
        BufferedImage image = null;

        String captchaType = captchaConfig.getType();
        // 生成验证码
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        }
        else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        // 存入缓存
        RedisUtil.setCacheObject(PIC_CODE_PREFIX + uuid, code, 180);

        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        res.put("uuid", uuid);
        res.put("img", Base64.encode(os.toByteArray()));
        return res;
    }

    /**
     * 验证图片验证码
     */
    public void validatePicCaptcha(String uuid, String code) {
        if (captchaConfig.getEnabled()) {
            if (!StringUtil.isAllNotBlank(uuid, code)) {
                throw new CaptchaException("captcha.notnull");
            }

            String picKey = PIC_CODE_PREFIX + uuid;

            String realCode = RedisUtil.getCacheObject(picKey);
            if (StringUtil.isBlank(realCode)) {
                throw new CaptchaException("captcha.expired");
            }
            if (!StringUtil.equalsIgnoreCase(code, realCode)) {
                throw new CaptchaException("captcha.error");
            }
            RedisUtil.deleteObject(picKey);
        }
    }

    /**
     * 发送短信验证码
     *
     * @param phoneNumber 手机号
     * @return 验证码id
     */
    public void sendSmsCode(String phoneNumber) {
        String smsKey = SMS_CODE_PREFIX + phoneNumber;
        if (RedisUtil.hasKey(smsKey)) {
            // 存在则不允许多次调用
            throw new BusinessExceptionI18n(BizCode.FREQUENT_RETRY.value(), "frequent.retry");
        }
        // TODO 调用SMS发送验证码
        String smsCode = RandomUtil.randomNumbers(6);
        RedisUtil.setCacheObject(smsKey, smsCode, 180);
    }

    /**
     * 校验短信验证码
     */
    public void validateSmsCode(@NonNull String phoneNumber, @NonNull String smsCode) {
        String smsKey = SMS_CODE_PREFIX + phoneNumber;

        String code = RedisUtil.getCacheObject(smsKey);
        if (StringUtil.isBlank(code)) {
            throw new CaptchaException("captcha.expired");
        }
        if (ObjUtil.notEqual(smsCode, code)) {
            throw new CaptchaException("captcha.error");
        }
        RedisUtil.deleteObject(smsKey);
    }

    /**
     * 校验最大重试次数
     */
    public void validateMaxTryCount(String username) {
        // 密码重试缓存key
        String lockKey = ACCOUNT_LOCK_PREFIX + username;
        Integer retryCount = ObjectUtil.defaultIfNull(RedisUtil.getCacheObject(lockKey), 0);
        if (retryCount > maxRetryCount) {
            throw new BusinessExceptionI18n(BizCode.FREQUENT_RETRY.value(), "frequent.retry");
        }
        ++retryCount;
        RedisUtil.setCacheObject(lockKey, retryCount, 300); // 默认冻结5分钟
    }

    /**
     * 账户解锁
     *
     * @param username 用户名
     */
    public void unlock(String username) {
        RedisUtil.deleteObject(ACCOUNT_LOCK_PREFIX + username);
    }

}

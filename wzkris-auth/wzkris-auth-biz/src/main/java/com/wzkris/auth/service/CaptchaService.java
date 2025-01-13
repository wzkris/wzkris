package com.wzkris.auth.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.google.code.kaptcha.Producer;
import com.wzkris.auth.config.CaptchaConfig;
import com.wzkris.auth.domain.vo.KaptchaVO;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.param.CaptchaException;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.common.security.oauth2.constants.CustomErrorCodes;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码服务
 *
 * @author wzkris
 */
@Slf4j
@Service
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
    public KaptchaVO createPicCode() throws IOException {
        boolean captchaEnabled = captchaConfig.getEnabled();
        KaptchaVO kaptchaVO = new KaptchaVO(captchaEnabled);
        if (!captchaEnabled) {
            return kaptchaVO;
        }

        String captchaType = captchaConfig.getType();
        String capStr, code;
        BufferedImage image;
        // 生成验证码
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        } else {
            throw new CaptchaException("captcha.type.unsupport");
        }

        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        String base64Img = Base64.encode(os.toByteArray());

        String uuid = IdUtil.simpleUUID();

        // 存入缓存
        RedisUtil.setObj(PIC_CODE_PREFIX + uuid, code, 90);

        kaptchaVO.setImg(base64Img);
        kaptchaVO.setUuid(uuid);
        kaptchaVO.setExpired(captchaConfig.getExpired());
        return kaptchaVO;
    }

    /**
     * 验证图片验证码
     */
    public void validatePicCode(String uuid, String code) {
        if (!StringUtil.isAllNotBlank(uuid, code)) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.PRECONDITION_FAILED.value(), CustomErrorCodes.VALIDATE_ERROR, "captcha.notnull");
        }

        String picKey = PIC_CODE_PREFIX + uuid;

        String realCode = RedisUtil.getObj(picKey);
        if (StringUtil.isBlank(realCode)) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.PRECONDITION_FAILED.value(), CustomErrorCodes.VALIDATE_ERROR, "captcha.expired");
        }
        if (!StringUtil.equalsIgnoreCase(code, realCode)) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.PRECONDITION_FAILED.value(), CustomErrorCodes.VALIDATE_ERROR, "captcha.error");
        }
        RedisUtil.delObj(picKey);
    }

    /**
     * 发送短信验证码
     *
     * @param phoneNumber 手机号
     * @return 过期时间
     */
    public int createSmsCode(@Nonnull String phoneNumber) {
        String smsKey = SMS_CODE_PREFIX + phoneNumber;
        if (RedisUtil.hasKey(smsKey)) {
            // 存在则不允许多次调用
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.TOO_MANY_REQUESTS.value(), CustomErrorCodes.FREQUENT_RETRY, "frequent.retry");
        }
        // TODO 调用SMS发送验证码
        String smsCode = RandomUtil.randomNumbers(6);
        log.info("手机号'{}'的短信验证码是：{}", phoneNumber, smsCode);
        RedisUtil.setObj(smsKey, smsCode, captchaConfig.getExpired());
        return captchaConfig.getExpired();
    }

    /**
     * 校验短信验证码
     */
    public void validateSmsCode(@NonNull String phoneNumber, @Nonnull String smsCode) {
        String smsKey = SMS_CODE_PREFIX + phoneNumber;

        String code = RedisUtil.getObj(smsKey);
        if (StringUtil.isBlank(code)) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.PRECONDITION_FAILED.value(), CustomErrorCodes.VALIDATE_ERROR, "captcha.expired");
        }
        if (ObjUtil.notEqual(smsCode, code)) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.PRECONDITION_FAILED.value(), CustomErrorCodes.VALIDATE_ERROR, "captcha.error");
        }
        RedisUtil.delObj(smsKey);
    }

    /**
     * 校验最大重试次数
     */
    public void validateMaxTryCount(String username) {
        // 密码重试缓存key
        String lockKey = ACCOUNT_LOCK_PREFIX + username;
        Integer retryCount = ObjectUtil.defaultIfNull(RedisUtil.getObj(lockKey), 0);
        if (retryCount > maxRetryCount) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.TOO_MANY_REQUESTS.value(), CustomErrorCodes.FREQUENT_RETRY, "frequent.retry");
        }
        ++retryCount;
        RedisUtil.setObj(lockKey, retryCount, 300); // 默认冻结5分钟
    }

}

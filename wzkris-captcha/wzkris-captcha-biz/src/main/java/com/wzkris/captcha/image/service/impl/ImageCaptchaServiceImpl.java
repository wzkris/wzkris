package com.wzkris.captcha.image.service.impl;

import com.wzkris.captcha.challenge.service.impl.ChallengeServiceImpl;
import com.wzkris.captcha.image.domain.ImageCaptchaData;
import com.wzkris.captcha.image.domain.ImageCaptchaInfo;
import com.wzkris.captcha.image.properties.ImageCaptchaProperties;
import com.wzkris.captcha.image.service.ImageCaptchaService;
import com.wzkris.captcha.image.store.ImageCaptchaStore;
import com.wzkris.common.core.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class ImageCaptchaServiceImpl implements ImageCaptchaService {

    private static final String CAPTCHA_ERROR = "invalidParameter.captcha.error";

    private static final String CODE_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final ImageCaptchaProperties captchaProperties;

    private final ImageCaptchaStore imageCaptchaStore;

    @Override
    public ImageCaptchaData createCaptcha() {
        String token = UUID.randomUUID().toString();
        String code = RandomStringUtils.secure().next(captchaProperties.getCodeLength(), CODE_CHARS);
        Date expires = Date.from(Instant.now().plus(captchaProperties.getCaptchaExpiresMs(), ChronoUnit.MILLIS));

        ImageCaptchaInfo captchaInfo = new ImageCaptchaInfo(code, expires);
        imageCaptchaStore.putCaptcha(token, captchaInfo);

        // 暂时使用模拟图片，实际应该使用真实的图片生成库
        ImageCaptchaData captchaData = new ImageCaptchaData();
        captchaData.setToken(token);
        captchaData.setImage("mock_image_data");
        captchaData.setExpires(expires);

        return captchaData;
    }

    @Override
    public String redeem(String token, String code) {
        Date now = new Date();
        ImageCaptchaInfo captchaInfo = imageCaptchaStore.removeCaptcha(token);
        if (Objects.isNull(captchaInfo) || !captchaInfo.getExpires().after(now)) {
            throw new IllegalArgumentException(CAPTCHA_ERROR);
        }

        if (!captchaInfo.getCode().equalsIgnoreCase(code)) {
            throw new IllegalArgumentException(CAPTCHA_ERROR);
        }

        String verToken = UUID.randomUUID().toString();
        Date expires = Date.from(now.toInstant().plus(captchaProperties.getTokenExpiresMs(), ChronoUnit.MILLIS));
        String hash = DigestUtils.sha256Hex(verToken);
        String id = RandomStringUtils.secure().next(captchaProperties.getIdSize(), ChallengeServiceImpl.HEX_STR);
        imageCaptchaStore.putToken(makeupToken(id, hash), expires);
        return makeupVerToken(id, verToken);
    }

    @Override
    public Boolean validateToken(String tokenStr) {
        if (StringUtil.isBlank(tokenStr)) {
            return false;
        }
        String[] splits = tokenStr.split(":", 2);
        if (splits.length != 2) {
            return false;
        }

        Date now = new Date();
        String id = splits[0];
        String verToken = splits[1];
        String hash = DigestUtils.sha256Hex(verToken);
        String tokenKey = makeupToken(id, hash);
        Date expires = imageCaptchaStore.removeToken(tokenKey);
        return Objects.nonNull(expires) && !expires.before(now);
    }

    private String makeupToken(String id, String hash) {
        return "%s:%s".formatted(id, hash);
    }

    private String makeupVerToken(String id, String verToken) {
        return "%s:%s".formatted(id, verToken);
    }

}
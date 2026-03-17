package com.wzkris.captcha.slide.service.impl;

import com.wzkris.captcha.challenge.service.impl.ChallengeServiceImpl;
import com.wzkris.captcha.slide.domain.SlideCaptchaData;
import com.wzkris.captcha.slide.domain.SlideCaptchaInfo;
import com.wzkris.captcha.slide.properties.SlideCaptchaProperties;
import com.wzkris.captcha.slide.service.SlideCaptchaService;
import com.wzkris.captcha.slide.store.SlideCaptchaStore;
import com.wzkris.common.core.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class SlideCaptchaServiceImpl implements SlideCaptchaService {

    private static final String CAPTCHA_ERROR = "invalidParameter.captcha.error";

    private final SlideCaptchaProperties captchaProperties;

    private final SlideCaptchaStore slideCaptchaStore;

    @Override
    public SlideCaptchaData createCaptcha() {
        String token = UUID.randomUUID().toString();
        int targetX = RandomUtils.nextInt(100, 300);
        Date expires = Date.from(Instant.now().plus(captchaProperties.getCaptchaExpiresMs(), ChronoUnit.MILLIS));

        SlideCaptchaInfo captchaInfo = new SlideCaptchaInfo(targetX, expires);
        slideCaptchaStore.putCaptcha(token, captchaInfo);

        // 暂时使用模拟图片，实际应该使用真实的图片生成库
        SlideCaptchaData captchaData = new SlideCaptchaData();
        captchaData.setToken(token);
        captchaData.setBackgroundImage("mock_background_image");
        captchaData.setSliderImage("mock_slider_image");
        captchaData.setExpires(expires);

        return captchaData;
    }

    @Override
    public String redeem(String token, Integer x) {
        Date now = new Date();
        SlideCaptchaInfo captchaInfo = slideCaptchaStore.removeCaptcha(token);
        if (Objects.isNull(captchaInfo) || !captchaInfo.getExpires().after(now)) {
            throw new IllegalArgumentException(CAPTCHA_ERROR);
        }

        int tolerance = captchaProperties.getTolerance();
        if (x < captchaInfo.getTargetX() - tolerance || x > captchaInfo.getTargetX() + tolerance) {
            throw new IllegalArgumentException(CAPTCHA_ERROR);
        }

        String verToken = UUID.randomUUID().toString();
        Date expires = Date.from(now.toInstant().plus(captchaProperties.getTokenExpiresMs(), ChronoUnit.MILLIS));
        String hash = DigestUtils.sha256Hex(verToken);
        String id = RandomStringUtils.secure().next(captchaProperties.getIdSize(), ChallengeServiceImpl.HEX_STR);
        slideCaptchaStore.putToken(makeupToken(id, hash), expires);
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
        Date expires = slideCaptchaStore.removeToken(tokenKey);
        return Objects.nonNull(expires) && !expires.before(now);
    }

    private String makeupToken(String id, String hash) {
        return "%s:%s".formatted(id, hash);
    }

    private String makeupVerToken(String id, String verToken) {
        return "%s:%s".formatted(id, verToken);
    }

}
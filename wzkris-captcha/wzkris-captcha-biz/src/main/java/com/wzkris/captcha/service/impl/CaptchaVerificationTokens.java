package com.wzkris.captcha.service.impl;

import com.wzkris.common.core.utils.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 图形 / 滑动 / 挑战验证码共用的校验票据签发与核销。
 */
final class CaptchaVerificationTokens {

    /**
     * 与挑战/图形/滑动验证码发放的校验 id 同源字符集
     */
    static final String HEX_STR = "0123456789abcdef";

    static final String CAPTCHA_ERROR = "invalidParameter.captcha.error";

    private CaptchaVerificationTokens() {
    }

    static IssuedVerificationToken issue(int idSize, long tokenExpiresMs, OffsetDateTime now,
                                         BiConsumer<String, OffsetDateTime> putToken) {
        String verToken = UUID.randomUUID().toString();
        OffsetDateTime expires = now.plus(tokenExpiresMs, ChronoUnit.MILLIS);
        String hash = DigestUtils.sha256Hex(verToken);
        String id = RandomStringUtils.secure().next(idSize, HEX_STR);
        putToken.accept(makeupToken(id, hash), expires);
        return new IssuedVerificationToken(makeupVerToken(id, verToken), expires);
    }

    static boolean validate(String tokenStr, Function<String, OffsetDateTime> removeToken) {
        if (StringUtil.isBlank(tokenStr)) {
            return false;
        }
        String[] splits = tokenStr.split(":", 2);
        if (splits.length != 2) {
            return false;
        }
        OffsetDateTime now = OffsetDateTime.now();
        String hash = DigestUtils.sha256Hex(splits[1]);
        String tokenKey = makeupToken(splits[0], hash);
        OffsetDateTime expires = removeToken.apply(tokenKey);
        return Objects.nonNull(expires) && !expires.isBefore(now);
    }

    private static String makeupToken(String id, String hash) {
        return "%s:%s".formatted(id, hash);
    }

    private static String makeupVerToken(String id, String verToken) {
        return "%s:%s".formatted(id, verToken);
    }

    record IssuedVerificationToken(String token, OffsetDateTime expires) {

    }

}

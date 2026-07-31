package com.wzkris.captcha.service.impl;

import com.wzkris.captcha.api.captcha.response.RedeemChallengeResponse;
import com.wzkris.captcha.domain.Challenge;
import com.wzkris.captcha.domain.ChallengeCaptchaInfo;
import com.wzkris.captcha.properties.ChallengeCaptchaProperties;
import com.wzkris.captcha.service.ChallengeService;
import com.wzkris.captcha.store.ChallengeCaptchaStore;
import com.wzkris.captcha.utils.CaptchaVerificationTokens;
import com.wzkris.common.core.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeCaptchaProperties captchaProperties;

    private final ChallengeCaptchaStore challengeCaptchaStore;

    public static String prng(String seed, int length) {
        if (StringUtils.isBlank(seed) || length <= 0) {
            throw new IllegalArgumentException("种子不能为空且长度必须大于0");
        }

        int state = fnv1a(seed);
        StringBuilder result = new StringBuilder(length);

        while (result.length() < length) {
            int rnd = next(state);
            state = rnd;
            result.append(toHexString(rnd));
        }

        return result.substring(0, length);
    }

    private static int fnv1a(String str) {
        int hash = 0x811c9dc5;
        for (int i = 0; i < str.length(); i++) {
            hash ^= str.charAt(i);
            hash += (hash << 1) + (hash << 4) + (hash << 7) + (hash << 8) + (hash << 24);
        }
        return hash;
    }

    private static int next(int state) {
        state ^= state << 13;
        state ^= state >>> 17;
        state ^= state << 5;
        return state;
    }

    private static String toHexString(int value) {
        String hex = Integer.toHexString(value);
        if (hex.length() < 8) {
            return StringUtils.leftPad(hex, 8, '0');
        }
        return hex;
    }

    @Override
    public ChallengeCaptchaInfo createChallenge() {
        int challengeCount = captchaProperties.getChallengeCount();
        int challengeSize = captchaProperties.getChallengeLength();
        int challengeDifficulty = captchaProperties.getChallengeDifficulty();
        long challengeExpiresMs = captchaProperties.getChallengeExpiresMs();
        String token = UUID.randomUUID().toString();
        OffsetDateTime expires = OffsetDateTime.now().plus(challengeExpiresMs, ChronoUnit.MILLIS);
        ChallengeCaptchaInfo challengeCaptchaInfo = new ChallengeCaptchaInfo(new Challenge(challengeCount, challengeSize, challengeDifficulty), expires, token);
        challengeCaptchaStore.putChallenge(token, challengeCaptchaInfo);
        return challengeCaptchaInfo;
    }

    @Override
    public RedeemChallengeResponse redeem(String token, List<Integer> solutions) {
        try {
            if (StringUtil.isBlank(token) || CollectionUtils.isEmpty(solutions)) {
                throw new IllegalArgumentException(CaptchaVerificationTokens.CAPTCHA_ERROR);
            }

            OffsetDateTime now = OffsetDateTime.now();
            ChallengeCaptchaInfo challengeCaptchaInfo = challengeCaptchaStore.removeChallenge(token);
            if (Objects.isNull(challengeCaptchaInfo) || !challengeCaptchaInfo.getExpires().isAfter(now)) {
                throw new IllegalArgumentException(CaptchaVerificationTokens.CAPTCHA_ERROR);
            }
            if (solutions.size() != captchaProperties.getChallengeCount()) {
                throw new IllegalArgumentException(CaptchaVerificationTokens.CAPTCHA_ERROR);
            }

            boolean isValid = IntStream.range(0, captchaProperties.getChallengeCount()).allMatch(i -> {
                String salt = prng("%s%d".formatted(token, i + 1), captchaProperties.getChallengeLength());
                String target = prng("%s%dd".formatted(token, i + 1), captchaProperties.getChallengeDifficulty());
                int solution = solutions.get(i);
                return DigestUtils.sha256Hex(salt + solution).startsWith(target);
            });
            if (!isValid) {
                throw new IllegalArgumentException(CaptchaVerificationTokens.CAPTCHA_ERROR);
            }

            CaptchaVerificationTokens.IssuedVerificationToken issued = CaptchaVerificationTokens.issue(
                    captchaProperties.getIdSize(),
                    captchaProperties.getTokenExpiresMs(),
                    now,
                    challengeCaptchaStore::putToken);
            return RedeemChallengeResponse.ok(issued.token(), issued.expires());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return RedeemChallengeResponse.error(e.getMessage());
        }
    }

    @Override
    public Boolean validateToken(String tokenStr) {
        return CaptchaVerificationTokens.validate(tokenStr, challengeCaptchaStore::removeToken);
    }

}

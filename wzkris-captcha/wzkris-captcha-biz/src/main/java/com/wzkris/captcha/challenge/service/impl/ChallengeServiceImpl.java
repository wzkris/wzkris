package com.wzkris.captcha.challenge.service.impl;

import com.wzkris.captcha.challenge.domain.Challenge;
import com.wzkris.captcha.challenge.domain.ChallengeData;
import com.wzkris.captcha.challenge.domain.RedeemChallengeResult;
import com.wzkris.captcha.challenge.properties.ChallengeCaptchaProperties;
import com.wzkris.captcha.challenge.service.ChallengeService;
import com.wzkris.captcha.challenge.store.ChallengeStore;
import com.wzkris.common.core.utils.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

public class ChallengeServiceImpl implements ChallengeService {

    public static final String HEX_STR = "0123456789abcdef";

    private static final String CAPTCHA_ERROR = "invalidParameter.captcha.error";

    private final ChallengeCaptchaProperties captchaProperties;

    private final ChallengeStore challengeStore;

    public ChallengeServiceImpl(ChallengeCaptchaProperties captchaProperties, ChallengeStore challengeStore) {
        this.captchaProperties = captchaProperties;
        this.challengeStore = challengeStore;
    }

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
    public ChallengeData createChallenge() {
        int challengeCount = captchaProperties.getChallengeCount();
        int challengeSize = captchaProperties.getChallengeLength();
        int challengeDifficulty = captchaProperties.getChallengeDifficulty();
        long challengeExpiresMs = captchaProperties.getChallengeExpiresMs();
        String token = UUID.randomUUID().toString();
        Date expires = Date.from(Instant.now().plus(challengeExpiresMs, ChronoUnit.MILLIS));
        ChallengeData challengeData = new ChallengeData(new Challenge(challengeCount, challengeSize, challengeDifficulty), expires, token);
        challengeStore.putChallenge(token, challengeData);
        return challengeData;
    }

    @Override
    public RedeemChallengeResult redeem(String token, List<Integer> solutions) {
        try {
            if (StringUtil.isBlank(token) || CollectionUtils.isEmpty(solutions)) {
                throw new IllegalArgumentException(CAPTCHA_ERROR);
            }

            Date now = new Date();
            ChallengeData challengeData = challengeStore.removeChallenge(token);
            if (Objects.isNull(challengeData) || !challengeData.getExpires().after(now)) {
                throw new IllegalArgumentException(CAPTCHA_ERROR);
            }
            if (solutions.size() != captchaProperties.getChallengeCount()) {
                throw new IllegalArgumentException(CAPTCHA_ERROR);
            }

            boolean isValid = IntStream.range(0, captchaProperties.getChallengeCount()).allMatch(i -> {
                String salt = prng("%s%d".formatted(token, i + 1), captchaProperties.getChallengeLength());
                String target = prng("%s%dd".formatted(token, i + 1), captchaProperties.getChallengeDifficulty());
                int solution = solutions.get(i);
                return DigestUtils.sha256Hex(salt + solution).startsWith(target);
            });
            if (!isValid) {
                throw new IllegalArgumentException(CAPTCHA_ERROR);
            }

            String verToken = UUID.randomUUID().toString();
            Date expires = Date.from(now.toInstant().plus(captchaProperties.getTokenExpiresMs(), ChronoUnit.MILLIS));
            String hash = DigestUtils.sha256Hex(verToken);
            String id = RandomStringUtils.secure().next(captchaProperties.getIdSize(), HEX_STR);
            challengeStore.putToken(makeupToken(id, hash), expires);
            return RedeemChallengeResult.ok(makeupVerToken(id, verToken), expires);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return RedeemChallengeResult.error(e.getMessage());
        }
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
        Date expires = challengeStore.removeToken(tokenKey);
        return Objects.nonNull(expires) && !expires.before(now);
    }

    private String makeupToken(String id, String hash) {
        return "%s:%s".formatted(id, hash);
    }

    private String makeupVerToken(String id, String verToken) {
        return "%s:%s".formatted(id, verToken);
    }

}

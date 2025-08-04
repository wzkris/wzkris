package com.wzkris.common.captcha.handler;

import com.wzkris.common.captcha.exception.ChallengeStoreException;
import com.wzkris.common.captcha.model.Challenge;
import com.wzkris.common.captcha.model.Token;
import com.wzkris.common.captcha.properties.CapProperties;
import com.wzkris.common.captcha.store.CapStore;
import com.wzkris.common.core.exception.captcha.CaptchaException;
import com.wzkris.common.core.utils.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.IntStream;

/**
 * cap 处理器
 *
 * @author wuhunyu
 * @date 2025/06/16 16:22
 **/
public class CapHandler {

    public static final String HEX_STR = "0123456789abcdef";

    private final CapProperties capProperties;

    private final CapStore capStore;

    public CapHandler(CapProperties capProperties, List<CapStore> capStoreList) {
        this.capProperties = capProperties;
        this.capStore = capStoreList.stream().filter(store -> store.storeType().name().equals(capProperties.getStoreType()))
                .findFirst().orElse(null);
    }

    public Challenge createChallenge() throws ChallengeStoreException {
        final int challengeCount = capProperties.getChallengeCount();
        final int challengeSize = capProperties.getChallengeSize();
        final int challengeDifficulty = capProperties.getChallengeDifficulty();
        final long challengeExpiresMs = capProperties.getChallengeExpiresMs();

        // 随机生成挑战
        List<List<String>> challenges = IntStream.range(0, challengeCount)
                .boxed()
                .map(i -> List.of(
                        RandomStringUtils.secure().next(challengeSize, HEX_STR),
                        RandomStringUtils.secure().next(challengeDifficulty, HEX_STR)
                ))
                .toList();

        // 生成 token
        final String token = UUID.randomUUID().toString();

        // 挑战过期时间
        final Date expires = Date.from(Instant.now()
                .plus(challengeExpiresMs, ChronoUnit.MILLIS));

        // 构建挑战对象
        final Challenge challenge = Challenge.builder()
                .challenge(challenges)
                .token(token)
                .expires(expires)
                .build();

        // 存储挑战
        if (!capStore.putChallenge(token, challenge)) {
            throw new ChallengeStoreException("Storage challenge failed, please try again later.");
        }

        return challenge;
    }

    public Token redeemChallenge(String token, List<List<Object>> solutions)
            throws IllegalArgumentException, IllegalStateException, ChallengeStoreException {
        if (StringUtil.isBlank(token) || CollectionUtils.isEmpty(solutions)) {
            throw new CaptchaException("captcha.error");
        }
        solutions = new ArrayList<>(solutions.subList(0, capProperties.getChallengeCount()));

        // 当前日期时间
        final Date now = new Date();

        // 移除 token
        final Challenge challenge = capStore.removeChallenge(token);
        if (Objects.isNull(challenge) || !challenge.getExpires().after(now)) {
            throw new CaptchaException("captcha.expired");
        }

        // 验证计算结果是否有效
        boolean isValid = false;
        outer:
        for (final List<String> challenges : challenge.getChallenge()) {
            final String salt = challenges.get(0);
            final String target = challenges.get(1);

            for (final List<Object> solution : solutions) {
                if (Objects.isNull(solution) || solution.size() != 3) {
                    throw new CaptchaException("captcha.error");
                }
                final Object s = solution.get(0);
                final Object t = solution.get(1);
                final Object ans = solution.get(2);
                if (Objects.equals(salt, s) &&
                        Objects.equals(target, t) &&
                        SHA256Helper.sha256Hex(salt + ans.toString())
                                .startsWith(target)
                ) {
                    isValid = true;
                    break outer;
                }
            }
        }

        if (!isValid) {
            throw new CaptchaException("captcha.error");
        }

        // 保存 token，用于后续验证
        final String verToken = UUID.randomUUID().toString();
        final Date expires = Date.from(now.toInstant().plus(capProperties.getTokenExpiresMs(), ChronoUnit.MILLIS));
        final String hash = SHA256Helper.sha256Hex(verToken);
        final String id = RandomStringUtils.secure().next(capProperties.getIdSize(), HEX_STR);
        if (!capStore.putToken(this.makeupToken(id, hash), expires)) {
            throw new ChallengeStoreException("Storage token failed, please try again later.");
        }

        // 构造验证凭证
        return Token.builder()
                .token(this.makeupVerToken(id, verToken))
                .expires(expires)
                .build();
    }

    private String makeupToken(String id, String hash) {
        return String.format("%s:%s", id, hash);
    }

    private String makeupVerToken(String id, String verToken) {
        return String.format("%s:%s", id, verToken);
    }

    public Boolean validateToken(String tokenStr) throws IllegalArgumentException {
        if (StringUtil.isBlank(tokenStr)) {
            return false;
        }

        // 提取 id 和 verToken
        final String[] splits = tokenStr.split(":", 2);
        if (splits.length < 2) {
            return false;
        }

        // 当前日期时间
        final Date now = new Date();

        final String id = splits[0];
        final String verToken = splits[1];
        final String hash = SHA256Helper.sha256Hex(verToken);
        final String tokenKey = this.makeupToken(id, hash);

        // 取出 token 的过期时间
        final Date expires = capStore.removeToken(tokenKey);
        return Objects.nonNull(expires) && !expires.before(now);
    }

}

package com.wzkris.common.captcha.handler;

import com.wzkris.common.captcha.exception.ChallengeStoreException;
import com.wzkris.common.captcha.model.Challenge;
import com.wzkris.common.captcha.model.ChallengeData;
import com.wzkris.common.captcha.model.Token;
import com.wzkris.common.captcha.properties.CapProperties;
import com.wzkris.common.captcha.store.CapStore;
import com.wzkris.common.core.exception.captcha.CaptchaException;
import com.wzkris.common.core.utils.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * cap 处理器
 *
 * @author wuhunyu
 * @date 2025/06/16 16:22
 **/
@Component
public class CapHandler {

    public static final String HEX_STR = "0123456789abcdef";

    private final CapProperties capProperties;

    private final CapStore capStore;

    public CapHandler(CapProperties capProperties, List<CapStore> capStoreList) {
        this.capProperties = capProperties;
        this.capStore = capStoreList.stream().filter(store -> store.storeType().name().equals(capProperties.getStoreType()))
                .findFirst().orElse(null);
    }

    public ChallengeData createChallenge() throws ChallengeStoreException {
        final int challengeCount = capProperties.getChallengeCount();
        final int challengeSize = capProperties.getChallengeLength();
        final int challengeDifficulty = capProperties.getChallengeDifficulty();
        final long challengeExpiresMs = capProperties.getChallengeExpiresMs();

        // 生成 token
        final String token = UUID.randomUUID().toString();

        // 挑战过期时间
        final Date expires = Date.from(Instant.now()
                .plus(challengeExpiresMs, ChronoUnit.MILLIS));

        // 构建挑战对象
        final ChallengeData challengeData = new ChallengeData(new Challenge(challengeCount, challengeSize, challengeDifficulty), expires, token);

        // 存储挑战
        if (!capStore.putChallenge(token, challengeData)) {
            throw new ChallengeStoreException("Storage challenge failed, please try again later.");
        }

        return challengeData;
    }

    public Token redeemChallenge(String token, List<Integer> solutions)
            throws IllegalArgumentException, IllegalStateException, ChallengeStoreException {
        if (StringUtil.isBlank(token) || CollectionUtils.isEmpty(solutions)) {
            throw new CaptchaException("invalidParameter.captcha.error");
        }

        // 当前日期时间
        final Date now = new Date();

        // 移除 token
        final ChallengeData challengeData = capStore.removeChallenge(token);
        if (Objects.isNull(challengeData) || !challengeData.getExpires().after(now)) {
            throw new CaptchaException("captcha.expired");
        }

        // 验证计算结果是否有效
        boolean isValid = IntStream.range(0, capProperties.getChallengeCount()).allMatch(i -> {
            String salt = prng("%s%d".formatted(token, i + 1), capProperties.getChallengeLength());
            String target = prng("%s%dd".formatted(token, i + 1), capProperties.getChallengeDifficulty());
            int solution = solutions.get(i);
            return DigestUtils.sha256Hex(salt + solution).startsWith(target);
        });

        if (!isValid) {
            throw new CaptchaException("invalidParameter.captcha.error");
        }

        // 保存 token，用于后续验证
        final String verToken = UUID.randomUUID().toString();
        final Date expires = Date.from(now.toInstant().plus(capProperties.getTokenExpiresMs(), ChronoUnit.MILLIS));
        final String hash = DigestUtils.sha256Hex(verToken);
        final String id = RandomStringUtils.secure().next(capProperties.getIdSize(), HEX_STR);
        if (!capStore.putToken(this.makeupToken(id, hash), expires)) {
            throw new ChallengeStoreException("Storage token failed, please try again later.");
        }

        // 构造验证凭证
        return new Token(this.makeupVerToken(id, verToken), expires);
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
            // 使用与JavaScript完全一致的十六进制转换
            result.append(toHexString(rnd));
        }

        return result.substring(0, length);
    }

    private static int fnv1a(String str) {
        int hash = 0x811c9dc5; // 2166136261
        for (int i = 0; i < str.length(); i++) {
            hash ^= str.charAt(i);
            // 乘以FNV倍数16777619，等价于移位相加
            // Java int的32位环绕特性确保与JavaScript完全一致
            hash += (hash << 1) + (hash << 4) + (hash << 7) + (hash << 8) + (hash << 24);
        }
        return hash; // 32位环绕自动处理，无需额外操作
    }

    private static int next(int state) {
        state ^= state << 13;
        state ^= state >>> 17;
        state ^= state << 5;
        return state;
    }

    private static String toHexString(int value) {
        // 将int转换为无符号32位值，然后转十六进制
        String hex = Integer.toHexString(value);
        // 补齐8位，与JavaScript的padStart(8, "0")一致
        if (hex.length() < 8) {
            return StringUtils.leftPad(hex, 8, '0');
        }
        return hex;
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
        if (splits.length != 2) {
            return false;
        }

        // 当前日期时间
        final Date now = new Date();

        final String id = splits[0];
        final String verToken = splits[1];
        final String hash = DigestUtils.sha256Hex(verToken);
        final String tokenKey = this.makeupToken(id, hash);

        // 取出 token 的过期时间
        final Date expires = capStore.removeToken(tokenKey);
        return Objects.nonNull(expires) && !expires.before(now);
    }

}

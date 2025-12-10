package com.wzkris.auth.service;

import com.wzkris.auth.config.MockProperties;
import com.wzkris.common.captcha.service.CapHandler;
import com.wzkris.common.captcha.config.CapProperties;
import com.wzkris.common.captcha.model.request.RedeemChallengeRequest;
import com.wzkris.common.captcha.model.response.RedeemChallengeResponse;
import com.wzkris.common.captcha.service.CapService;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.exception.captcha.CaptchaException;
import com.wzkris.common.core.exception.request.TooManyRequestException;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.redis.util.RedisUtil;
import org.redisson.api.RScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
public class CaptchaService extends CapService {

    private static final String LOCK_PREFIX = "captcha:lock:";

    private static final String VALIDATE_PREFIX = "captcha:validate:";

    private static final String MAXTRY_PREFIX = "captcha:max_try:";

    private final MockProperties mockProperties;

    public CaptchaService(CapHandler capHandler, CapProperties capProperties, MockProperties mockProperties) {
        super(capHandler, capProperties);
        this.mockProperties = mockProperties;
    }

    /**
     * 冻结账号
     *
     * @param key     唯一标识
     * @param timeout 冻结时长（秒）
     */
    public void freezeAccount(String key, int timeout) {
        RedisUtil.setObj(LOCK_PREFIX + key, "", timeout);
    }

    /**
     * 校验账号是否被冻结
     */
    public void validateAccount(String key) {
        if (RedisUtil.exist(LOCK_PREFIX + key)) {
            throw new CaptchaException(BizBaseCodeEnum.TOO_MANY_REQUESTS.value(), "service.internalError.busy");
        }
    }

    /**
     * 校验验证码
     */
    public boolean validateCaptcha(String key, String code) {
        if (mockProperties.getCaptchaValidateMock()) {
            return true;
        }
        String fullKey = VALIDATE_PREFIX + key;
        String realcode = RedisUtil.getObj(fullKey);
        if (StringUtil.isBlank(realcode)) {
            return false;
        }
        if (!StringUtil.equals(realcode, code)) {
            return false;
        }
        return RedisUtil.delObj(fullKey);
    }

    /**
     * 验证最大尝试次数
     *
     * @param key     唯一标识
     * @param maxTry  最大尝试次数, 超出则抛出异常
     * @param timeout 超时时长（秒）
     */
    public void validateMaxTry(String key, int maxTry, int timeout) {
        if (mockProperties.getCaptchaMaxtryMock()) {
            return;
        }
        // 构建 Redis 中的键名
        String counterKey = MAXTRY_PREFIX + key;

        // 定义 Lua 脚本
        String luaScript = "local currentTry = redis.call('get', KEYS[1]) or 0 "
                + "if tonumber(currentTry) >= tonumber(ARGV[1]) then "
                + "    return 0 "
                + "else "
                + "    redis.call('incr', KEYS[1]) "
                + "    redis.call('expire', KEYS[1], ARGV[2]) "
                + "    return 1 "
                + "end";

        // 执行 Lua 脚本
        RScript script = RedisUtil.getScript();
        Long result = script.eval(
                RScript.Mode.READ_WRITE,
                luaScript,
                RScript.ReturnType.INTEGER,
                Collections.singletonList(counterKey),
                maxTry,
                timeout);

        // 检查结果
        if (result.intValue() == 0) {
            throw new TooManyRequestException();
        }
    }

    @Override
    public RedeemChallengeResponse redeemChallenge(RedeemChallengeRequest redeemChallengeRequest) {
        if (mockProperties.getCaptchaValidateMock()) {
            return new RedeemChallengeResponse(true, null, "mock", new Date(System.currentTimeMillis() + 300_000));
        }
        return super.redeemChallenge(redeemChallengeRequest);
    }

    @Override
    public Boolean validateChallenge(String token) {
        if (mockProperties.getCaptchaValidateMock()) {
            return true;
        }
        return super.validateChallenge(token);
    }

    @Override
    public void setCaptcha(String key, String code) {
        if (mockProperties.getCaptchaValidateMock()) {
            return;
        }
        super.setCaptcha(key, code);
    }

}

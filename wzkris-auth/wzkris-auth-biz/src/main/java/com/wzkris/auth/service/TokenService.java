package com.wzkris.auth.service;

import com.wzkris.auth.security.config.TokenProperties;
import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.redis.util.RedisUtil;
import jakarta.annotation.Nullable;
import org.redisson.api.RBatch;
import org.redisson.api.RBucketAsync;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

/**
 * token操作
 *
 * @author wzkris
 */
@Component
public class TokenService {

    private static final String GET_CORE_USER_SCRIPT = """
            local userId = redis.call('GET', KEYS[1])
            
            if not userId then
                return nil
            end
            
            local content = userId:match('^"(.*)"$')
            if content then
                userId = content
            end
            
            return redis.call('GET', KEYS[2] .. userId)
            """;// 处理一下双引号

    private final RedissonClient redissonClient = RedisUtil.getClient();

    private final String USER_INFO_PREFIX = "user_info:";

    private final String USER_INFO_ACCESS_TOKEN_PREFIX = "user_info:access_token:";

    private final String USER_INFO_REFRESH_TOKEN_PREFIX = "user_info:refresh_token:";

    @Autowired
    private TokenProperties tokenProperties;

    public final void save(CorePrincipal principal, @Nullable String accessToken, @Nullable String refreshToken) {
        RBatch batch = redissonClient.createBatch();

        RBucketAsync<CorePrincipal> userinfo = batch.getBucket(USER_INFO_PREFIX + principal.getId());
        userinfo.setAsync(principal, Duration.ofSeconds(tokenProperties.getUserRefreshTokenTimeOut()));

        if (StringUtil.isNotBlank(accessToken)) {
            RBucketAsync<String> accesstoken = batch.getBucket(USER_INFO_ACCESS_TOKEN_PREFIX + accessToken);
            accesstoken.setAsync(principal.getId(), Duration.ofSeconds(tokenProperties.getUserTokenTimeOut()));
        }

        if (StringUtil.isNotBlank(refreshToken)) {
            RBucketAsync<String> refreshtoken = batch.getBucket(USER_INFO_REFRESH_TOKEN_PREFIX + refreshToken);
            refreshtoken.setAsync(principal.getId(), Duration.ofSeconds(tokenProperties.getUserRefreshTokenTimeOut()));
        }

        batch.execute();
    }

    @Nullable
    public final CorePrincipal loadByAccessToken(String accessToken) {
        return redissonClient.getScript().eval(RScript.Mode.READ_ONLY, GET_CORE_USER_SCRIPT,
                RScript.ReturnType.VALUE, List.of(USER_INFO_ACCESS_TOKEN_PREFIX + accessToken, USER_INFO_PREFIX));
    }

    @Nullable
    public final CorePrincipal loadByRefreshToken(String refreshToken) {
        return redissonClient.getScript().eval(RScript.Mode.READ_ONLY, GET_CORE_USER_SCRIPT,
                RScript.ReturnType.VALUE, List.of(USER_INFO_REFRESH_TOKEN_PREFIX + refreshToken, USER_INFO_PREFIX));
    }

}

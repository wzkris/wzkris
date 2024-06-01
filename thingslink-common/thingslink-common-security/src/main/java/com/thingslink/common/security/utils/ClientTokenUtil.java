package com.thingslink.common.security.utils;


import cn.hutool.http.HttpUtil;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.exception.ThirdServiceException;
import com.thingslink.common.core.utils.SpringUtil;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.redis.util.RedisUtil;
import com.thingslink.common.security.oauth2.config.OAuth2Properties;
import com.thingslink.common.security.oauth2.constants.OAuth2SecurityConstants;
import org.redisson.api.RBucket;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 客户端token工具类
 * @date : 2024/05/20 9:13
 */
public class ClientTokenUtil {

    private static final String client_token_key = "client_token";

    private static final OAuth2Properties O_AUTH_2_PROPERTIES = SpringUtil.getBean(OAuth2Properties.class);

    /**
     * 获取当前客户端的client_token
     *
     * @return client_token
     */
    public static String getClientToken() {
        RBucket<String> tokenBucket = RedisUtil.getClient().getBucket(client_token_key);
        long ttl = TimeUnit.MILLISECONDS.toSeconds(tokenBucket.remainTimeToLive());

        // 小于等于10s
        if (ttl <= 10) {
            Map<String, Object> paramMap = Map.of(
                    OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.CLIENT_CREDENTIALS.getValue(),
                    OAuth2ParameterNames.CLIENT_ID, O_AUTH_2_PROPERTIES.getClientid(),
                    OAuth2ParameterNames.CLIENT_SECRET, O_AUTH_2_PROPERTIES.getClientSecret(),
                    OAuth2ParameterNames.SCOPE, OAuth2SecurityConstants.SCOPE_INNER_REQUEST
            );
            Result<Map<String, Object>> result = JsonUtil.parseObject(HttpUtil.post(O_AUTH_2_PROPERTIES.getClientTokenUri(), paramMap), Result.class);
            Map<String, Object> tokenMap = result.checkData();
            if (tokenMap == null) {
                throw new ThirdServiceException(503, "service.unavailable");
            }
            String token = tokenMap.getOrDefault(OAuth2ParameterNames.ACCESS_TOKEN, "").toString();
            String tokenType = tokenMap.getOrDefault(OAuth2ParameterNames.TOKEN_TYPE, "").toString();
            int expired = Integer.parseInt(tokenMap.get(OAuth2ParameterNames.EXPIRES_IN).toString());

            tokenBucket.set(tokenType + StringUtil.SPACE + token, Duration.ofSeconds(expired));
        }

        return tokenBucket.get();
    }

}

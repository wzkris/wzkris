package com.wzkris.common.notifier.dingtalk.client;

import com.wzkris.common.core.enums.BizCallCodeEnum;
import com.wzkris.common.core.exception.service.ExternalServiceException;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.notifier.dingtalk.domain.TokenCache;
import com.wzkris.common.notifier.properties.DingtalkProperties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 钉钉 API 客户端
 * 负责钉钉 API 的底层调用，包括配置管理、Token 管理和消息发送
 *
 * @author wzkris
 * @date 2025/11/06
 */
@Slf4j
public class DingtalkApiClient {

    private static final String HOST = "https://api.dingtalk.com";

    private final DingtalkProperties dingtalkProperties;

    private final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    /**
     * AccessToken缓存，key为appKey
     */
    private final Map<String, TokenCache> tokenCacheMap = new ConcurrentHashMap<>();

    public DingtalkApiClient(DingtalkProperties dingtalkProperties) {
        Assert.notNull(dingtalkProperties, "钉钉配置不能为空");
        this.dingtalkProperties = dingtalkProperties;
    }

    /**
     * 获取 AccessToken（带缓存）
     *
     * @param appKey    应用Key
     * @param appSecret 应用Secret
     * @return AccessToken
     */
    private String getAccessToken(String appKey, String appSecret) {
        TokenCache tokenCache = tokenCacheMap.get(appKey);

        if (tokenCache != null && tokenCache.getExpireTime() > System.currentTimeMillis() + 3_000) {
            return tokenCache.getToken();
        }

        Request request = new Request.Builder()
                .url(HOST + "/v1.0/oauth2/accessToken")
                .post(RequestBody.create(
                        JsonUtil.toJsonString(Map.of(
                                "appKey", appKey,
                                "appSecret", appSecret)),
                        MediaType.parse("application/json")
                ))
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "未知错误";
                throw new ExternalServiceException(BizCallCodeEnum.DINGTALK_ERROR.value(), errorBody);
            }

            ResponseBody responseBody = response.body();
            Map<String, Object> res = JsonUtil.toMap(
                    responseBody.string(), Map.class, String.class, Object.class);
            String accessToken = res.get("accessToken").toString();
            long expireIn = Long.parseLong(res.get("expireIn").toString());

            TokenCache newCache = new TokenCache(accessToken, expireIn * 1_000 + System.currentTimeMillis());
            tokenCacheMap.put(appKey, newCache);

            log.debug("获取钉钉 AccessToken 成功，appKey: {}", appKey);
            return accessToken;
        } catch (IOException e) {
            throw new RuntimeException("获取钉钉AccessToken失败", e);
        }
    }

    /**
     * 发送群消息
     */
    public String sendMessage(List<String> userIds, String msgKey, String msgParam) {
        String appKey = dingtalkProperties.getAppKey();
        String appSecret = dingtalkProperties.getAppSecret();
        Assert.hasText(appKey, "钉钉appKey不能为空");
        Assert.hasText(appSecret, "钉钉appSecret不能为空");

        String accessToken = getAccessToken(appKey, appSecret);
        return sendMessage(accessToken, dingtalkProperties.getRobotCode(), userIds, msgKey, msgParam);
    }

    /**
     * 发送群消息（底层方法）
     *
     * @param accessToken AccessToken
     * @param robotCode   机器人Code
     * @param msgKey      消息Key
     * @param msgParam    消息参数（JSON字符串）
     * @return 消息ID（processQueryKey）
     */
    private String sendMessage(String accessToken, String robotCode, List<String> userIds, String msgKey, String msgParam) {
        String requestBody = JsonUtil.toJsonString(Map.of(
                "msgKey", msgKey,
                "msgParam", msgParam,
                "robotCode", robotCode,
                "userIds", userIds
        ));

        Request request = new Request.Builder()
                .url(HOST + "/v1.0/robot/oToMessages/batchSend")
                .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                .header("x-acs-dingtalk-access-token", accessToken)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "未知错误";
                throw new ExternalServiceException(BizCallCodeEnum.DINGTALK_ERROR.value(), errorBody);
            }

            ResponseBody responseBody = response.body();
            Map<String, Object> res = JsonUtil.toMap(
                    responseBody.string(), Map.class, String.class, Object.class);
            String messageId = res.get("processQueryKey").toString();

            log.debug("发送钉钉群消息成功，messageId: {}", messageId);
            return messageId;
        } catch (IOException e) {
            throw new RuntimeException("发送钉钉消息失败", e);
        }
    }

}

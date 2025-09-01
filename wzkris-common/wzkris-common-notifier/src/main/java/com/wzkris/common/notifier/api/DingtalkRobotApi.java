package com.wzkris.common.notifier.api;

import com.aliyun.tea.okhttp.OkHttpClientBuilder;
import com.wzkris.common.core.enums.BizCallCode;
import com.wzkris.common.core.exception.service.ExternalServiceException;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.notifier.properties.DingtalkProperties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;

/**
 * 钉钉机器人消息
 *
 * @author wzkris
 * @date 2025/8/13
 */
@Slf4j
public class DingtalkRobotApi {

    private static final String HOST = "https://api.dingtalk.com";

    private final DingtalkProperties.AppProperties appProperties;

    private final OkHttpClient okHttpClient = new OkHttpClientBuilder()
            .buildOkHttpClient();

    private String accessToken;

    private Long accessTokenExpireTime;

    private DingtalkRobotApi(DingtalkProperties.AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public static DingtalkRobotApi getInstance(DingtalkProperties.AppProperties appProperties) {
        Assert.notNull(appProperties, "钉钉的app配置不能为空");
        return new DingtalkRobotApi(appProperties);
    }

    public String getAccessToken() {
        if (this.accessToken != null && this.accessTokenExpireTime > System.currentTimeMillis() + 3_000) {
            return this.accessToken;
        }
        Request request = new Request.Builder()
                .url(HOST + "/v1.0/oauth2/accessToken")
                .post(RequestBody.create(
                        JsonUtil.toJsonString(Map.of("appKey", appProperties.getAppKey(), "appSecret", appProperties.getAppSecret())),
                        MediaType.parse("application/json")
                ))
                .build();
        Call call = okHttpClient.newCall(request);
        try (Response response = call.execute()) {
            if (!response.isSuccessful()) {
                throw new ExternalServiceException(BizCallCode.DINGTALK_ERROR.value(), response.body().string());
            }
            ResponseBody responseBody = response.body();
            Map<String, Object> res = JsonUtil.toMap(responseBody.string(), Map.class, String.class, Object.class);
            this.accessToken = res.get("accessToken").toString();
            this.accessTokenExpireTime = Long.parseLong(res.get("expireIn").toString()) * 1_000 + System.currentTimeMillis();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this.accessToken;
    }

    /**
     * @param msgKey
     * @param msgParam
     * @return 加密消息id，根据此id可查询消息已读状态和撤回消息。
     */
    public String sendGroupMessage(String msgKey, String msgParam) {
        Request request = new Request.Builder()
                .url(HOST + "/v1.0/robot/groupMessages/send")
                .post(RequestBody.create(
                        JsonUtil.toJsonString(Map.of("msgKey", msgKey, "msgParam", """
                                        {
                                              "content": %s
                                          }
                                        """.formatted(msgParam),
                                "robotCode", appProperties.getRobotCode())),
                        MediaType.parse("application/json")
                ))
                .header("x-acs-dingtalk-access-token", getAccessToken())
                .build();
        Call call = okHttpClient.newCall(request);
        try (Response response = call.execute()) {
            if (!response.isSuccessful()) {
                throw new ExternalServiceException(BizCallCode.DINGTALK_ERROR.value(), response.body().string());
            }
            ResponseBody responseBody = response.body();
            Map<String, Object> res = JsonUtil.toMap(responseBody.string(), Map.class, String.class, Object.class);
            return res.get("processQueryKey").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

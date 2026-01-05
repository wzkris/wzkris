package com.wzkris.common.notifier.client;

import com.wzkris.common.core.enums.BizCallCodeEnum;
import com.wzkris.common.core.exception.service.ExternalServiceException;
import com.wzkris.common.core.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;

/**
 * 钉钉 API 客户端
 * 负责钉钉 API 的消息调用，包括配置管理、Token 管理和消息发送
 *
 * @author wzkris
 * @date 2025/11/06
 */
@Slf4j
public class DingtalkMsgClient {

    private final String robotWebhook;

    private final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    public DingtalkMsgClient(String robotWebhook) {
        Assert.notNull(robotWebhook, "钉钉配置不能为空");
        this.robotWebhook = robotWebhook;
    }

    /**
     * 发送机器人群消息
     *
     * @param msgtype  消息类型（如：text, markdown）
     * @param msgParam 消息参数（JSON字符串，将直接作为requestBody）
     * @return 响应消息（成功时返回"ok"）
     */
    public String sendGroupMessage(String msgtype, String msgParam) {
        String requestBody = JsonUtil.toJsonString(Map.of(
                "msgtype", msgtype,
                msgtype, msgParam,
                "at", Map.of("isAtAll", true)
        ));

        Request request = new Request.Builder()
                .url(robotWebhook)
                .post(RequestBody.create(requestBody, MediaType.parse("application/json; charset=utf-8")))
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            String responseStr = responseBody != null ? responseBody.string() : "";
            Map<String, Object> res = JsonUtil.toMap(responseStr, Map.class, String.class, Object.class);

            // Webhook API响应格式: {"errcode": 0, "errmsg": "ok"}
            Object errcode = res.get("errcode");
            Object errmsg = res.get("errmsg");

            if (errcode != null && Integer.parseInt(errcode.toString()) == 0) {
                return errmsg != null ? errmsg.toString() : "ok";
            } else {
                String errorMsg = String.format("发送钉钉群消息失败，errcode: %s, errmsg: %s", errcode, errmsg);
                throw new ExternalServiceException(BizCallCodeEnum.DINGTALK_ERROR.value(), errorMsg);
            }
        } catch (IOException e) {
            throw new RuntimeException("发送钉钉消息失败", e);
        }
    }

}

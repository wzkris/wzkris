package com.wzkris.common.notifier;

import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import com.wzkris.common.notifier.config.dingtalk.client.DingtalkApiClient;
import com.wzkris.common.notifier.config.dingtalk.properties.DingtalkProperties;
import com.wzkris.common.notifier.domain.DingtalkMessage;
import com.wzkris.common.notifier.domain.NotificationResult;
import com.wzkris.common.notifier.enums.DingtalkTemplateKey;
import com.wzkris.common.notifier.impl.DingtalkNotifier;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import shade.com.alibaba.fastjson2.JSONObject;

import java.util.List;
import java.util.Map;

@Slf4j
public class DingtalkStreamTest {

    public static void main(String[] args) throws Exception {
        OpenDingTalkStreamClientBuilder
                .custom()
                .credential(new AuthClientCredential("dingdqyhmmuczhzoss2a", "wyV7fwA_35dzbIlkqENlVU6yaRePbPAjK0H1hUKz4xKq-qmxcY-tHfD5WqVyyz9q"))
                //注册机器人监听器
                .registerCallbackListener("/v1.0/im/bot/messages/get", robotMessage -> {
                    log.info("receive robotMessage, {}", robotMessage);
                    //开发者根据自身业务需求，处理机器人回调
                    return new JSONObject();
                })
                .build().start();
    }

    @Test
    public void apitest() {
        DingtalkProperties dingtalkProperties = new DingtalkProperties();
        dingtalkProperties.setAppKey("111");
        dingtalkProperties.setAppSecret("222");
        DingtalkApiClient apiClient = new DingtalkApiClient(dingtalkProperties);
        DingtalkNotifier robotApi = new DingtalkNotifier(apiClient);
        DingtalkMessage message = DingtalkMessage.builder()
                .templateKey(DingtalkTemplateKey.MARKDOWN)
                .recipients(List.of("100"))
                .templateParams(Map.of("title", "钉钉通知", "text", "这是一条通过钉钉机器人发送的测试消息。"))
                .build();
        NotificationResult notificationResult = robotApi.send(message);
        log.info("{}", notificationResult);
    }

}

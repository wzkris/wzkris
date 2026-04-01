package com.wzkris.common.notifier;

import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import com.wzkris.common.notifier.client.DingtalkMsgClient;
import com.wzkris.common.notifier.core.impl.DingtalkNotifier;
import com.wzkris.common.notifier.domain.DingtalkMessage;
import com.wzkris.common.notifier.core.NotificationResult;
import com.wzkris.common.notifier.enums.DingtalkTemplateKeyEnum;
import com.wzkris.common.notifier.properties.NotifierProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import shade.com.alibaba.fastjson2.JSONObject;

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

    static DingtalkMsgClient msgClient = new DingtalkMsgClient();

    static DingtalkNotifier robotApi = new DingtalkNotifier(msgClient, new NotifierProperties());

    @Test
    public void dingtalkSend() {
        DingtalkMessage message = DingtalkMessage.builder()
                .templateKey(DingtalkTemplateKeyEnum.MARKDOWN)
                .templateParams(Map.of("title", "钉钉通知", "text", "这是一条通过钉钉机器人发送的测试消息。"))
                .build();
        NotificationResult notificationResult = robotApi.send(message);
        log.info("{}", notificationResult);
    }

    @Test
    public void dingtalkSend2() {
        DingtalkMessage message = DingtalkMessage.builder()
                .templateKey(DingtalkTemplateKeyEnum.LINK)
                .templateParams(Map.of("title", "钉钉通知", "text", "这是一条通过钉钉机器人发送的测试消息。",
                        "picUrl", "百度", "messageUrl", "https://www.baidu.com"))
                .build();
        NotificationResult notificationResult = robotApi.send(message);
        log.info("{}", notificationResult);
    }

}

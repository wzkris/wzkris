package com.wzkris.common.notifier;

import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import com.wzkris.common.notifier.api.DingtalkRobotApi;
import com.wzkris.common.notifier.properties.DingtalkProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import shade.com.alibaba.fastjson2.JSONObject;

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
        DingtalkProperties.AppProperties appProperties = new DingtalkProperties.AppProperties();
        DingtalkRobotApi robotApi = DingtalkRobotApi.getInstance(appProperties);
        String res = robotApi.sendGroupMessage("sampleText", "你好");
        log.info(res);
    }

}

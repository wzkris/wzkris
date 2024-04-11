package org.mock.device.util;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.mock.device.util.MqttUtil;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : mqtt回调
 * @date : 2023/4/29 14:47
 */
@Slf4j
public class MqttCallback implements org.eclipse.paho.client.mqttv3.MqttCallback {

    @Override
    public void connectionLost(Throwable cause) {
        log.error("mqtt连接丢失,异常信息：", cause);
        MqttUtil.init();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        log.info("接收到消息：{}，来自主题：{}", new String(message.getPayload()), topic);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("消息发送完成");
    }
}

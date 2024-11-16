package com.wzkris.equipment.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 接收MQTT消息
 * @date : 2023/4/29 14:47
 */
@Slf4j
public class MqttReceive implements MqttCallback {

    @Override
    public void connectionLost(Throwable cause) {
        if (cause instanceof MqttException e) {
            log.error("mqtt连接丢失,异常信息：{}, 错误代码：{}", e.getMessage(), e.getReasonCode(), cause);
        }
        else {
            log.error("mqtt连接丢失,异常信息：{}", cause.getMessage(), cause);
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {
            String data = new String(message.getPayload());

            log.info("接收到消息：{}，来自主题：{}", data, topic);
        }
        catch (Exception e) {
            log.error("接收到消息处理异常，异常信息：{}", e.getMessage(), e);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.debug("消息发送完成");
    }
}

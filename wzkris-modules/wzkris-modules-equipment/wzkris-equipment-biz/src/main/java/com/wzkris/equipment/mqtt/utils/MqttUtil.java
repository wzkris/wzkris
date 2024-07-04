package com.wzkris.equipment.mqtt.utils;

import cn.hutool.core.util.ObjUtil;
import com.wzkris.equipment.mqtt.constants.MqttTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.nio.charset.StandardCharsets;


/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : mqtt服务
 * @date : 2023/4/29 14:36
 */
@Slf4j
public class MqttUtil {
    private static MqttClient mqttClient;

    public static void setClient(MqttClient mqttClient) {
        MqttUtil.mqttClient = mqttClient;
    }

    /**
     * 发布，默认qos为0，非持久化
     */
    public static <T> void publish(String topic, T message) {
        publish(topic, message, 0);
    }

    /**
     * 发布消息，非持久化
     */
    public static <T> void publish(String topic, T message, int qos) {
        try {
            if (StringUtils.isEmpty(topic) || ObjUtil.isEmpty(message)) {
                log.error("消息主题和发送消息不能为空");
                return;
            }
            mqttClient.publish(topic, message.toString().getBytes(StandardCharsets.UTF_8), qos, false);
        }
        catch (MqttException e) {
            log.error("发布消息时发生异常，errmsg：{}", e.getMessage());
        }
    }

    /**
     * 订阅默认主题
     */
    public static void subDefaultTopic() {
        // 订阅队列 加号为占位符
        try {
            mqttClient.subscribe(String.format(MqttTopic.CAR_ATTR, "+", "+"), 1);
            mqttClient.subscribe(String.format(MqttTopic.CAR_SERVICE_REPLY, "+", "+"), 1);
            mqttClient.subscribe(String.format(MqttTopic.CAR_EVENT, "+", "+"), 1);
            mqttClient.subscribe("$SYS/brokers/+/clients/+/+", 2);// 上下线
            mqttClient.subscribe("$SYS/brokers/+/alarms/#", 2);// 系统告警
        }
        catch (MqttException e) {
            log.error("订阅主题时发生异常，errmsg：{}", e.getMessage());
        }
    }

    /**
     * 重连
     */
    public static void reconnect() {
        try {
            mqttClient.reconnect();
        }
        catch (MqttException e) {
            log.error("重连时发生异常，errmsg：{}", e.getMessage());
        }
    }

}

package org.mock.device.util;

import cn.hutool.core.util.ObjUtil;
import com.thingslink.common.core.utils.SpringUtil;
import com.thingslink.common.mqtt.constant.MqttTopic;
import io.micrometer.common.util.StringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.mock.device.config.MqttProperties;

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

    /**
     * 初始化方法
     */
    public static void init() {
        MqttProperties mqttProperties = SpringUtil.getBean(MqttProperties.class);

        try {
            mqttClient = new MqttClient(mqttProperties.getHost(), mqttProperties.getClientId(), new MemoryPersistence());
            // 创建链接参数
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            // 在重新启动和重新连接时记住状态
            connectOptions.setCleanSession(false);
            // 设置连接的用户名
            connectOptions.setUserName(mqttProperties.getUsername());
            connectOptions.setPassword(mqttProperties.getPassword().toCharArray());
            connectOptions.setConnectionTimeout(mqttProperties.getTimeout());
            connectOptions.setKeepAliveInterval(mqttProperties.getKeepAlive());
            mqttClient.setCallback(new MqttCallback());
            // 建立连接
            log.info("mqtt连接初始化");
            mqttClient.connect(connectOptions);

            // 订阅队列 加号为占位符
            mqttClient.subscribe(String.format(MqttTopic.CAR_AUTH_REPLY, "+", "+"));
            mqttClient.subscribe(String.format(MqttTopic.CAR_ATTR_REPLY, "+", "+"));
            mqttClient.subscribe(String.format(MqttTopic.CAR_SERVICE, "+", "+"));
            mqttClient.subscribe(String.format(MqttTopic.CAR_EVENT_REPLY, "+", "+"));
        }
        catch (MqttException e) {
            log.error("mqtt客户端初始化失败，errmsg：{}", e.getMessage(), e);
        }
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
    @SneakyThrows
    public static <T> void publish(String topic, T message, int qos) {
        if (!mqttClient.isConnected()) {
            log.error("mqtt未连接");
            return;
        }
        if (StringUtils.isEmpty(topic) || ObjUtil.isEmpty(message)) {
            log.error("消息主题和发送消息不能为空");
            return;
        }
        mqttClient.publish(topic, message.toString().getBytes(StandardCharsets.UTF_8), qos, false);
    }

    /**
     * 订阅某个主题
     */
    @SneakyThrows
    public static void subscribe(String topic, int qos) {
        if (!mqttClient.isConnected()) {
            log.error("mqtt未连接");
            return;
        }
        mqttClient.subscribe(topic, qos);
    }

    /**
     * 取消订阅主题
     *
     * @param topic 主题名称
     */
    @SneakyThrows
    public static void unsubscribe(String topic) {
        if (!mqttClient.isConnected()) {
            log.error("mqtt未连接");
            return;
        }
        mqttClient.unsubscribe(topic);
    }
}

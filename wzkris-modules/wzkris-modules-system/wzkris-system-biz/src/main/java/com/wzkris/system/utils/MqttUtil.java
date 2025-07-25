package com.wzkris.system.utils;

import com.wzkris.system.mqtt.MqttProperties;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : MQTT工具
 * @date : 2023/4/29 14:36
 * @update: 2024/11/16 14:45
 */
@Slf4j
@Component
@ConditionalOnProperty("mqtt.enable")
public class MqttUtil {

    private static final Map<String, MqttClientPool> mqttPools = new HashMap<>();

    @Autowired
    private MqttProperties mqttProperties;

    /**
     * 轮询方式获取客户端
     */
    @Nonnull
    private static MqttClientPool get(final String key) {
        return mqttPools.get(key);
    }

    /**
     * 订阅主题
     */
    public static void sub(final String key, final String topic, final int qos) {
        get(key).sub(topic, qos);
    }

    @PostConstruct
    public void init() throws MqttException, UnknownHostException {
        Map<String, MqttProperties.GeneralSetting> settings = mqttProperties.getSettings();

        for (Map.Entry<String, MqttProperties.GeneralSetting> setting : settings.entrySet()) {
            MqttProperties.GeneralSetting generalSetting = setting.getValue();
            String clientidPref = generalSetting.getClientId() + "_" + setting.getKey() + "_"
                    + InetAddress.getLocalHost().getHostAddress(); // 解决集群部署clientid冲突
            final MqttClientPool pool = new MqttClientPool();
            for (int i = 0; i < generalSetting.getClientNum(); i++) {
                MqttClient mqttClient =
                        new MqttClient(generalSetting.getHost(), clientidPref + "_" + i, new MemoryPersistence());
                // 创建链接参数
                MqttConnectOptions connectOptions = new MqttConnectOptions();
                connectOptions.setAutomaticReconnect(generalSetting.getAutomaticReconnect()); // 自动重连
                connectOptions.setMaxReconnectDelay(generalSetting.getMaxReconnectDelay()); // 重连最大间隔
                // 在重新启动和重新连接时记住状态
                connectOptions.setCleanSession(generalSetting.getCleanSession());
                // 设置连接的用户名
                connectOptions.setUserName(generalSetting.getUsername());
                connectOptions.setPassword(generalSetting.getPassword().toCharArray());
                connectOptions.setConnectionTimeout(generalSetting.getConnectTimeout());
                connectOptions.setKeepAliveInterval(generalSetting.getKeepAlive());
                connectOptions.setMaxInflight(generalSetting.getMaxInFlight());
                // 建立连接
                mqttClient.connect(connectOptions);
                log.info("-------------------mqtt客户端：{}, 第{}个连接初始化-------------------", setting.getKey(), i + 1);
                pool.add(mqttClient);
            }
            mqttPools.put(setting.getKey(), pool);
        }
    }

    /**
     * MQTT客户端池
     */
    public static class MqttClientPool {

        private final List<MqttClient> instance = new ArrayList<>();

        private int round = 0;

        private void add(MqttClient client) {
            instance.add(client);
        }

        /**
         * 轮询获取MQTT客户端
         */
        private synchronized MqttClient getClient() {
            MqttClient mqttClient = instance.get(round);
            if (round < mqttPools.size() - 1) round++;
            else round = 0;
            return mqttClient;
        }

        /**
         * 发布qos0
         */
        public final boolean pubQ0(final String topic, final String message, long sendTimeout) {
            return pub(topic, message, 0, sendTimeout);
        }

        /**
         * 发布qos1
         */
        public final boolean pubQ1(final String topic, final String message, long sendTimeout) {
            return pub(topic, message, 1, sendTimeout);
        }

        /**
         * 发布qos2
         */
        public final boolean pubQ2(final String topic, final String message, long sendTimeout) {
            return pub(topic, message, 2, sendTimeout);
        }

        /**
         * 发布消息
         */
        public final boolean pub(final String topic, final String msg, final int qos, long sendTimeout) {
            try {
                org.eclipse.paho.client.mqttv3.MqttTopic mqttTopic = getClient().getTopic(topic);
                MqttMessage message = new MqttMessage();
                message.setRetained(false);
                message.setQos(qos);
                message.setPayload(msg.getBytes(StandardCharsets.UTF_8));
                MqttDeliveryToken deliveryToken = mqttTopic.publish(message);
                deliveryToken.waitForCompletion(sendTimeout); // 等待操作完成
                return deliveryToken.getException() == null; // 为空代表发送成功
            } catch (MqttException e) {
                log.error("发布消息时发生异常，errmsg：{}", e.getMessage());
                return false;
            }
        }

        /**
         * 订阅主题
         */
        public final void sub(final String topic, final int qos) {
            try {
                for (MqttClient mqttClient : instance) {
                    mqttClient.subscribe(topic, qos);
                }
            } catch (MqttException e) {
                log.error("订阅主题时发生异常，errmsg：{}", e.getMessage());
            }
        }

    }

}

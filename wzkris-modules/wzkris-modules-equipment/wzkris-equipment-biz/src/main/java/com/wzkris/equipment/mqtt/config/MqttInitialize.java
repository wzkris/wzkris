package com.wzkris.equipment.mqtt.config;

import com.wzkris.equipment.mqtt.MqttMsgProducer;
import com.wzkris.equipment.mqtt.utils.MqttUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : MQTT初始化
 * @date : 2023/12/4 14:08
 */
@Slf4j
//@Configuration
public class MqttInitialize implements InitializingBean {

    @Autowired
    private MqttProperties mqttProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        MqttClient mqttClient = new MqttClient(mqttProperties.getHost(), mqttProperties.getClientId(), new MemoryPersistence());
        // 创建链接参数
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        // 在重新启动和重新连接时记住状态
        connectOptions.setCleanSession(false);
        // 设置连接的用户名
        connectOptions.setUserName(mqttProperties.getUsername());
        connectOptions.setPassword(mqttProperties.getPassword().toCharArray());
        connectOptions.setConnectionTimeout(mqttProperties.getTimeout());
        connectOptions.setKeepAliveInterval(mqttProperties.getKeepAlive());
        mqttClient.setCallback(new MqttMsgProducer());
        // 建立连接
        log.info("-------------------mqtt连接初始化-------------------");
        mqttClient.connect(connectOptions);
        // 工具类持有单例客户端
        MqttUtil.setClient(mqttClient);
        MqttUtil.subDefaultTopic();
    }
}

package com.wzkris.equipment.mqtt.init;

import com.wzkris.equipment.mqtt.constants.MqttTopic;
import com.wzkris.equipment.mqtt.utils.MqttUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : MQTT初始化
 * @date : 2023/12/4 14:08
 * @update: 2024/11/16 14:45
 */
@Slf4j
@Configuration
@ConditionalOnBean(MqttUtil.class)
public class MqttInitialize implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        MqttUtil.sub("car", String.format(MqttTopic.CAR_ATTR, "+", "+"), 1);
        MqttUtil.sub("car", String.format(MqttTopic.CAR_SERVICE_REPLY, "+", "+"), 1);
        MqttUtil.sub("car", String.format(MqttTopic.CAR_EVENT, "+", "+"), 1);
        MqttUtil.sub("car", "$SYS/brokers/+/clients/+/+", 2);// 上下线
        MqttUtil.sub("car", "$SYS/brokers/+/alarms/#", 2);// 系统告警
    }
}

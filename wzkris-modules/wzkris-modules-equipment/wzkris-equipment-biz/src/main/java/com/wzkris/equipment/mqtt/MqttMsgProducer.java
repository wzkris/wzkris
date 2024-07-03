package com.wzkris.equipment.mqtt;

import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.equipment.domain.dto.MessageDTO;
import com.wzkris.equipment.mqtt.config.MqttInitialize;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : mqtt消息生产者
 * @date : 2023/4/29 14:47
 */
@Slf4j
public class MqttMsgProducer implements MqttCallback {

    @Override
    public void connectionLost(Throwable cause) {
        log.error("mqtt连接丢失,异常信息：{}", cause.getMessage(), cause);
        MqttInitialize mqttInitialize = SpringUtil.getBean(MqttInitialize.class);
        try {
            mqttInitialize.afterPropertiesSet();
        }
        catch (Exception e) {
            // 连接丢失则不断重试
            this.connectionLost(e);
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {
            String data = new String(message.getPayload());

            String[] topicSp = topic.split("/");
            if (topic.startsWith("$SYS/brokers/")) {
                if (StringUtil.equals("connected", topicSp[5])) {
                    // 上线
                    String clientid = topicSp[4];
                    MessageDTO messageDTO = new MessageDTO(clientid, null, message.getPayload());
                }
                else if (StringUtil.equals("disconnected", topicSp[5])) {
                    // 下线
                    String clientid = topicSp[4].split(":")[1];
                    MessageDTO messageDTO = new MessageDTO(clientid, null, message.getPayload());
                }
            }
            else if (topic.startsWith("device/car")) {
                // 交换密钥
                MessageDTO messageDTO = new MessageDTO(topicSp[3], topicSp[2], message.getPayload());
            }
            // ....

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

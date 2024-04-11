package com.thingslink.equipment.mqtt;

import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.equipment.domain.dto.MessageDTO;
import com.thingslink.equipment.mqtt.utils.ConsumerUtil;
import com.thingslink.equipment.mqtt.utils.MqttUtil;
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
public class MqttMessageProducer implements MqttCallback {

    @Override
    public void connectionLost(Throwable cause) {
        log.error("mqtt连接丢失,异常信息：{}", cause.getMessage(), cause);
        MqttUtil.init();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {
            String data = new String(message.getPayload());

            String[] topicSp = topic.split("/");

            boolean offer = false;

            if (StringUtil.equals("$SYS", topicSp[0]) && StringUtil.equals("disconnected", topicSp[5])) {
                // 设备上下线，由于设备上线需要入网许可，所以mqtt这里只处理下线情况
                String serialNo = topicSp[4].split(":")[1];
                MessageDTO messageDTO = new MessageDTO(serialNo, null, message.getPayload());
                offer = ConsumerUtil.getUpDownQueue().offer(messageDTO);
            }
            else if (StringUtil.equals("auth", topicSp[4])) {
                // 获取入网许可
                MessageDTO messageDTO = new MessageDTO(topicSp[3], topicSp[2], message.getPayload());
                offer = ConsumerUtil.getAuthQueue().offer(messageDTO);
            }
            // ....

            log.info("接收到消息：{}，来自主题：{}，插入队列：{}", data, topic, offer);
        }
        catch (Exception e) {
            log.error("接收到消息异常，异常信息：{}", e.getMessage(), e);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("消息发送完成");
    }
}

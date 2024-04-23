package com.thingslink.equipment.mqtt.constants;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : mqtt主题  mock_topic
 * @date : 2023/4/29 15:36
 */
public interface MqttTopic {
    // 入网默认pk
    String DEFAULT_CAR_P_K = "car_charging_pile";

    /**
     * %s 占位符可以根据实际情况进行替换 这里简单点我会直接拿设备的sn号替换
     * 最终队列会变成 device/car/{random_key}/{sn}/auth/post
     */

    // 充电桩汽车入网队列
    String CAR_AUTH = "device/car/%s/%s/auth/post";
    // 入网回应队列
    String CAR_AUTH_REPLY = "device/car/%s/%s/auth/post_reply";

    // 充电桩汽车属性队列 (属性是设备的功能模型，用于描述设备运行时的状态)
    String CAR_ATTR = "device/car/%s/%s/property/request";
    // 充电桩汽车属性回应队列
    String CAR_ATTR_REPLY = "device/car/%s/%s/property/request_reply";

    // 充电桩汽车服务队列 (服务是设备的行为模型，表示设备可被外部调用的能力 如充电桩启动充电、停止充电等)
    String CAR_SERVICE = "device/car/%s/%s/service/set";
    String CAR_SERVICE_REPLY = "device/car/%s/%s/service/set_reply";

    // 充电桩汽车事件队列 (服务是设备的现实模型，包含需要被外部感知和处理的通知信息 如设备发生故障或告警时上送事件信息)
    String CAR_EVENT = "device/car/%s/%s/event/raw_up";
    String CAR_EVENT_REPLY = "device/car/%s/%s/event/raw_up_reply";

}

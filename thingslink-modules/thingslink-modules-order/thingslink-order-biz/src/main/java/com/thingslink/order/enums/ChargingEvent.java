package com.thingslink.order.enums;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 充电订单事件
 * @date : 2023/4/27 11:07
 */
public enum ChargingEvent {
    /**
     * 支付
     */
    PAY,
    /**
     * 取消订单
     */
    CANCEL_ORDER,
    /**
     * 打开充电
     */
    OPEN_CHARGE,
    /**
     * 停止充电
     */
    STOP_CHARGE,
}

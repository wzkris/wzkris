package com.thingslink.order.enums;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 充电订单状态
 * @date : 2023/2/3 10:29
 */
public enum ChargingStatus {
    NOTPAY("未支付"),
    SUCCESS("支付成功"),
    CLOSED("订单关闭"),
    CHARGING("充电中"),
    CHARGE_END("充电结束"),
    ERROR("支付异常");

    ChargingStatus(String desc) {
    }
}
package com.thingslink.order.enums;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 支付方式
 * @date : 2023/2/3 10:31
 */
public enum PayType {
    WALLET("钱包支付"),
    WECHAT("微信支付"),
    ZFB("支付宝");

    PayType(String desc) {
    }
}

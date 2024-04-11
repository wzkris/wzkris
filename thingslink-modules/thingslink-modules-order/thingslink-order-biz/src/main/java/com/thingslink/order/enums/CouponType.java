package com.thingslink.order.enums;

import lombok.AllArgsConstructor;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 优惠券类型
 * @date : 2023/2/5 9:45
 */
@AllArgsConstructor
public enum CouponType {
    PLATFORM("1", "平台类型"),
    MERCHANT("2", "商户类型");

    private final String code;
    private final String desc;

    public String value() {
        return this.code;
    }
}

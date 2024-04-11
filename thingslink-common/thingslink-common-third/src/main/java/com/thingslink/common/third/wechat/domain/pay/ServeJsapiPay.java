package com.thingslink.common.third.wechat.domain.pay;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : jsapi下单--服务商模式
 * @date : 2023/5/23 14:24
 */
@Data
@Accessors(chain = true)
public class ServeJsapiPay {
    /**
     * 子商户appid
     */
    private String subAppId;
    /**
     * 子商户号
     */
    private String subMchId;
    /**
     * 商品描述
     */
    private String description;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 微信openid
     */
    private String openId;
    /**
     * 金额，单位 分
     */
    private Integer amount;
}

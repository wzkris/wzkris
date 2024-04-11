package com.thingslink.common.third.wechat.config;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 默认微信支付配置
 * @date : 2023/5/22 9:42
 */
@Data
@FieldNameConstants
@Configuration
@ConfigurationProperties(prefix = "wechat.pay")
public class WechatPayConfig {
    /**
     * appid
     */
    private String appId;
    /**
     * appSecret
     */
    private String appSecret;
    /**
     * 商户号
     */
    private String merchantId;
    /**
     * 商户API私钥路径
     */
    private String privateKeyPath;
    /**
     * 商户证书序列号
     */
    private String merchantSerialNumber;
    /**
     * v3密钥
     */
    private String apiV3key;
    /**
     * 支付回调地址
     */
    private String payCallbackUrl;
    /**
     * 退款回调地址
     */
    private String refundCallbackUrl;
}

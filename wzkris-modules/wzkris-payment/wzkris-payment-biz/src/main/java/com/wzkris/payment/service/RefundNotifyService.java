package com.wzkris.payment.service;

import java.util.Map;

/**
 * 渠道【退款】异步回调服务。支付回调见 {@link PayNotifyService}。
 *
 * @author wzkris
 */
public interface RefundNotifyService {

    /**
     * 处理渠道【退款】异步回调，返回回渠道的应答串。
     *
     * @param configId 渠道商户配置ID（路径携带，多商户按配置精确路由验签/解密；渠道由配置派生）
     */
    String handle(Long configId, String body, Map<String, String> headers);

}

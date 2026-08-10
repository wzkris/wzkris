package com.wzkris.payment.service;

import java.util.Map;

/**
 * 渠道【支付】异步回调服务。退款回调见 {@link RefundNotifyService}。
 *
 * @author wzkris
 */
public interface PayNotifyService {

    /**
     * 处理渠道【支付】异步回调，返回回渠道的应答串。
     *
     * @param configId 渠道商户配置ID（路径携带，多商户按配置精确路由验签/解密；渠道由配置派生）
     */
    String handle(Long configId, String body, Map<String, String> headers);

}

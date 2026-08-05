package com.wzkris.payment.service;

import com.wzkris.payment.enums.channel.PayChannelEnum;

import java.util.Map;

/**
 * 渠道回调编排
 *
 * @author wzkris
 */
public interface PayNotifyService {

    /**
     * 处理渠道异步回调，返回回渠道的应答串
     *
     * @param configId 渠道商户配置ID（路径携带，多商户按配置精确路由验签/解密）
     */
    String handleNotify(PayChannelEnum channel, Long configId, String body, Map<String, String> headers);

}

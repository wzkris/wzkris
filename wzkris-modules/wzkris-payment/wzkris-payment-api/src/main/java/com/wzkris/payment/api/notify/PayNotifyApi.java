package com.wzkris.payment.api.notify;

import com.wzkris.payment.enums.channel.PayChannelEnum;

import java.util.Map;

/**
 * 渠道【支付】异步回调接口（渠道 -> 网关）。退款回调见 {@link RefundNotifyApi}。
 *
 * <p>返回回渠道的应答串（微信 v3 JSON / 支付宝 "success" 等），内容由各渠道决定。
 *
 * @author wzkris
 */
public interface PayNotifyApi {

    /**
     * 处理渠道【支付】异步回调，返回回渠道的应答串。
     *
     * @param configId 渠道商户配置ID（路径携带，多商户按配置精确路由验签/解密）
     */
    String handlePayNotify(PayChannelEnum channel, Long configId, String body, Map<String, String> headers);

}

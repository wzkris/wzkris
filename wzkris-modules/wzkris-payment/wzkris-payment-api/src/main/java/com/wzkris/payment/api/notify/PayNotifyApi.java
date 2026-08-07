package com.wzkris.payment.api.notify;

import com.wzkris.payment.enums.channel.PayChannelEnum;

import java.util.Map;

/**
 * 渠道异步回调接口（渠道 -> 网关）。
 *
 * <p>编排顺序：先落库原始报文 -> 验签/解密 -> 幂等 -> 锁内状态机，返回回渠道的 ACK 串。
 * ACK 内容由各渠道决定（微信 v3 JSON / 支付宝 "success" 等）。
 *
 * @author wzkris
 */
public interface PayNotifyApi {

    /**
     * 处理渠道异步回调，返回回渠道的应答串。
     *
     * @param configId 渠道商户配置ID（路径携带，多商户按配置精确路由验签/解密）
     */
    String handleNotify(PayChannelEnum channel, Long configId, String body, Map<String, String> headers);
}

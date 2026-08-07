package com.wzkris.payment.provider;

import com.wzkris.payment.domain.PayChannelConfigDO;

/**
 * 渠道路由上下文：策略实现 + 对应渠道配置
 *
 * @author wzkris
 */
public record ProviderContext(PaymentProvider provider, PayChannelConfigDO config) {

}

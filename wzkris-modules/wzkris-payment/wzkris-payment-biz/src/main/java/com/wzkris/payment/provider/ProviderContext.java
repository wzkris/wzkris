package com.wzkris.payment.provider;

import com.wzkris.payment.domain.PayChannelConfigDO;
import lombok.AllArgsConstructor;

/**
 * 渠道路由上下文：策略实现 + 对应渠道配置
 *
 * @author wzkris
 */
@AllArgsConstructor
public record ProviderContext(PaymentProvider provider, PayChannelConfigDO config) {

}

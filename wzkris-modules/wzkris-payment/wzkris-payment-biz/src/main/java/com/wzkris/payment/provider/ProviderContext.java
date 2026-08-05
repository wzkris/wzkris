package com.wzkris.payment.provider;

import com.wzkris.payment.domain.PayChannelConfigDO;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 渠道路由上下文：策略实现 + 对应渠道配置
 *
 * @author wzkris
 */
@Getter
@AllArgsConstructor
public class ProviderContext {

    private final PaymentProvider provider;

    private final PayChannelConfigDO config;

}

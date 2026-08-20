package com.wzkris.payment.provider.model;

import com.wzkris.payment.domain.PayChannelConfigDO;
import com.wzkris.payment.provider.PayChannelProvider;

/**
 * 已配对的渠道策略与商户配置：由 {@code PaymentProviderRouter#resolve} 尽早产出，后续 SPI 调用与回调解析
 * 一律透传本对象（provider 用于验签/构造 ACK，config 携带商户密钥与回调地址），不再分别传双参。
 *
 * @author wzkris
 */
public record PaymentProviderContext(PayChannelProvider provider, PayChannelConfigDO config) {

}

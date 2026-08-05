package com.wzkris.payment.provider;

import com.wzkris.common.core.exception.service.BusinessException;
import com.wzkris.payment.domain.PayChannelConfigDO;
import com.wzkris.payment.enums.channel.ChannelStatusEnum;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.service.PayChannelConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 渠道路由器：按渠道取策略实现 + 指定商户配置
 *
 * @author wzkris
 */
@Component
@RequiredArgsConstructor
public class PaymentProviderRouter {

    private final List<PaymentProvider> providers;

    private final PayChannelConfigService configService;

    /**
     * 由调用方指定 configId（下单/退款/关单/回调均走此重载）。
     * 多商户场景下不同配置各有商户号与密钥，必须精确路由，不能取"任一启用配置"。
     */
    public ProviderContext resolve(PayChannelEnum channel, @Nullable Long configId) {
        if (configId == null) {
            throw new BusinessException(99902, "未指定渠道配置:" + channel);
        }
        PaymentProvider provider = providers.stream()
                .filter(p -> p.channel() == channel)
                .findFirst()
                .orElseThrow(() -> new BusinessException(99902, "不支持的支付渠道:" + channel));
        PayChannelConfigDO config = configService.getById(configId);
        if (config == null) {
            throw new BusinessException(99902, "渠道配置不存在:" + configId);
        }
        if (config.getStatus() != ChannelStatusEnum.ENABLED) {
            throw new BusinessException(99902, "渠道配置已停用:" + channel);
        }
        if (config.getChannel() != channel) {
            throw new BusinessException(99902, "配置与渠道不匹配:" + channel);
        }
        return new ProviderContext(provider, config);
    }
}

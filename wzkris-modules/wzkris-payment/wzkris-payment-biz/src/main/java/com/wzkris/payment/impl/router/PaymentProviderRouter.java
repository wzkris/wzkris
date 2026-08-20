package com.wzkris.payment.impl.router;

import com.wzkris.payment.domain.PayChannelConfigDO;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.provider.PayChannelProvider;
import com.wzkris.payment.provider.model.ChannelResult;
import com.wzkris.payment.provider.model.PaymentProviderContext;
import com.wzkris.payment.service.PayChannelConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 渠道路由器：按渠道取策略实现 + 配对指定商户配置
 *
 * <p>路由失败(配置缺失/渠道不匹配/provider 缺失)以 {@link ChannelResult#fail} 返回,由编排层转友好响应,不抛异常。
 * 不校验 {@code ENABLED} 状态：回调链路需处理停用商户的已成交订单,是否校验由各消费方按自身语义决定。
 *
 * @author wzkris
 */
@Component
@RequiredArgsConstructor
public class PaymentProviderRouter {

    private final PayChannelConfigService configService;

    private final List<PayChannelProvider> providers;

    /**
     * 按渠道取策略实现（不查 DB）。回调路径在路由配置前即需 provider 构造 ACK。
     */
    public Optional<PayChannelProvider> findProvider(PayChannelEnum channel) {
        return providers.stream()
                .filter(p -> p.channel() == channel)
                .findFirst();
    }

    /**
     * 渠道 + 商户配置配对：配置存在 + 渠道对应的 provider 两项校验，供主动链路与回调链路共用。
     * 渠道由配置派生（config.channel），无需调用方另传。配置与请求渠道是否一致由各消费方按自身语义校验。
     *
     * @return 成功时为 {@link PaymentProviderContext}，失败时为 {@link ChannelResult#fail}
     */
    public ChannelResult<PaymentProviderContext> resolve(Long configId) {
        PayChannelConfigDO config = configService.getById(configId);
        if (config == null) {
            return ChannelResult.fail("渠道配置不存在");
        }
        Optional<PayChannelProvider> providerOpt = findProvider(config.getChannel());
        if (providerOpt.isEmpty()) {
            return ChannelResult.fail("不支持的支付渠道");
        }
        return ChannelResult.ok(new PaymentProviderContext(providerOpt.get(), config));
    }

}

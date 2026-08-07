package com.wzkris.payment.job;

import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.provider.PaymentProviderRouter;
import com.wzkris.payment.provider.ProviderContext;
import com.wzkris.payment.service.PayOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 超时订单关单巡检：扫描已过期仍 PENDING 的订单，渠道关单后置 CLOSED。
 *
 * <p>顺序为先渠道关单、再条件置 CLOSED（PENDING->CLOSED）：
 * <ul>
 *   <li>渠道关单成功后才落 CLOSED，避免渠道侧仍可支付却本地已关导致丢单；</li>
 *   <li>渠道关单失败（含渠道侧已支付）则保持 PENDING，由异步回调推进为 SUCCESS，或下轮重试。</li>
 * </ul>
 *
 * <p>P0 用 @Scheduled（WebAutoConfiguration 已 @EnableScheduling）；P6 切 XXL-Job 分布式调度
 *
 * @author wzkris
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PayOrderExpireJob {

    private final PayOrderService payOrderService;

    private final PaymentProviderRouter router;

    @Scheduled(fixedDelay = 60_000L)
    public void sweep() {
        List<PayOrderDO> orders = payOrderService.findExpiredPending(100);
        for (PayOrderDO order : orders) {
            try {
                ProviderContext ctx = router.resolve(order.getChannel(), order.getConfigId());
                ctx.provider().close(order, ctx.config());
                payOrderService.updateToClosed(order.getId());
            } catch (Exception e) {
                // 关单失败（含渠道侧已支付）：保持 PENDING，由回调推进或下轮重试，不中断其他订单
                log.warn("关单失败 payOrderId={} : {}", order.getId(), e.getMessage());
            }
        }
    }

}

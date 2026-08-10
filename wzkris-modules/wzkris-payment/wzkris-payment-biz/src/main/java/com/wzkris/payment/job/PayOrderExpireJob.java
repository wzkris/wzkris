// com.wzkris.payment.job.PayOrderExpireJob.java
package com.wzkris.payment.job;

import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.impl.router.PaymentProviderRouter;
import com.wzkris.payment.provider.model.ChannelResult;
import com.wzkris.payment.provider.model.PaymentProviderContext;
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
                // 1. 配对渠道+商户配置（resolve 校验配置存在/provider，渠道由配置派生，不校验 ENABLED）
                ChannelResult<PaymentProviderContext> resolved = router.resolve(order.getConfigId());
                if (!resolved.success()) {
                    log.warn("关单失败 payOrderId={} : {}", order.getId(), resolved.errMsg());
                    continue;
                }
                PaymentProviderContext ctx = resolved.data();

                // 2. 调用渠道关单
                ChannelResult<Void> closeResult = ctx.provider().close(order, ctx);
                if (!closeResult.success()) {
                    log.warn("关单失败 payOrderId={} : {}", order.getId(), closeResult.errMsg());
                    continue;
                }

                // 3. 条件更新：仅 PENDING 状态可流转为 CLOSED，防止并发回调已支付
                boolean updated = payOrderService.updateToClosed(order.getId());
                if (updated) {
                    log.info("关单成功 payOrderId={} orderNo={}", order.getId(), order.getOrderNo());
                } else {
                    log.info("关单时订单状态已变更 payOrderId={} orderNo={}", order.getId(), order.getOrderNo());
                }
            } catch (Exception e) {
                // 兜底：DB 异常等不可预期错误，保持 PENDING，不中断其他订单
                log.warn("关单异常 payOrderId={} : {}", order.getId(), e.getMessage(), e);
            }
        }
    }

}
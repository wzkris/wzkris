package com.wzkris.payment.impl.order;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.payment.api.order.PayOrderApi;
import com.wzkris.payment.api.order.request.PrepayRequest;
import com.wzkris.payment.api.order.response.PayOrderResponse;
import com.wzkris.payment.api.order.response.PrepayResponse;
import com.wzkris.payment.config.PaymentProperties;
import com.wzkris.payment.domain.PayChannelLogDO;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.enums.channel.ChannelLogStatusEnum;
import com.wzkris.payment.enums.pay.PayStatusEnum;
import com.wzkris.payment.provider.PaymentProviderRouter;
import com.wzkris.payment.provider.ProviderContext;
import com.wzkris.payment.provider.model.PrepayResult;
import com.wzkris.payment.service.PayChannelLogService;
import com.wzkris.payment.service.PayOrderService;
import com.wzkris.payment.util.OrderNoGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

/**
 * 支付订单（业务方调用）：统一下单 + 订单查询
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class PayOrderApiImpl extends AbstractApi implements PayOrderApi {

    private final PayOrderService payOrderService;

    private final PayChannelLogService payChannelLogService;

    private final PaymentProviderRouter router;

    private final OrderNoGenerator orderNoGenerator;

    private final PaymentProperties properties;

    @Override
    public Result<PrepayResponse> prepay(PrepayRequest request) {
        // 幂等：同 bizType+bizNo 已有订单则复用，DB 部分唯一索引兜底并发
        PayOrderDO exist = payOrderService.getByBizTypeAndBizNo(request.getBizType(), request.getBizNo());
        if (exist != null) {
            if (exist.getStatus() == PayStatusEnum.SUCCESS) {
                return requestFail("订单已支付");
            }
            if (exist.getStatus() != PayStatusEnum.PENDING) {
                return requestFail("订单状态不允许支付");
            }
            // 复用原订单成交配置
            ProviderContext ctx = router.resolve(exist.getChannel(), exist.getConfigId());
            return doPrepay(exist, ctx);
        }

        // 先路由配置，订单快照 config_id；订单落库为独立短事务，渠道调用在其外
        ProviderContext ctx = router.resolve(request.getChannel(), request.getConfigId());
        PayOrderDO order = new PayOrderDO();
        order.setOrderNo(orderNoGenerator.nextOrderNo());
        order.setBizType(request.getBizType());
        order.setBizNo(request.getBizNo());
        order.setChannel(request.getChannel());
        order.setConfigId(ctx.config().getId());
        order.setPayMode(request.getPayMode());
        order.setSubject(request.getSubject());
        order.setAmount(request.getAmount());
        order.setPayerId(request.getPayerId());
        order.setClientIp(request.getClientIp());
        order.setNotifyUrl(request.getNotifyUrl());
        order.setStatus(PayStatusEnum.PENDING);
        int expireMin = request.getExpireMinutes() != null
                ? request.getExpireMinutes()
                : properties.getDefaultExpireMinutes();
        order.setExpireAt(OffsetDateTime.now().plusMinutes(expireMin));
        payOrderService.save(order);
        return doPrepay(order, ctx);
    }

    /**
     * 渠道预下单并留痕交互记录（渠道调用在 DB 事务之外）。
     *
     * <p>渠道异常时记 FAILED 日志并返回失败：订单仍为 PENDING，业务方可凭 bizType+bizNo 幂等重试，
     * 超时未支付的订单由关单巡检任务回收。
     */
    private Result<PrepayResponse> doPrepay(PayOrderDO order, ProviderContext ctx) {
        PayChannelLogDO channelLog = new PayChannelLogDO();
        channelLog.setPayOrderId(order.getId());
        channelLog.setChannel(order.getChannel());
        channelLog.setConfigId(ctx.config().getId());
        channelLog.setPayMode(order.getPayMode());
        channelLog.setRequestParams(JsonUtil.toJsonString(order));
        try {
            PrepayResult prepayResult = ctx.provider().prepay(order, ctx.config());
            channelLog.setResponseParams(JsonUtil.toJsonString(prepayResult));
            channelLog.setStatus(ChannelLogStatusEnum.SUCCESS);
            payChannelLogService.save(channelLog);
            PrepayResponse resp = new PrepayResponse();
            resp.setId(order.getId());
            resp.setOrderNo(order.getOrderNo());
            resp.setChannel(order.getChannel());
            resp.setPayMode(order.getPayMode());
            resp.setPrepayPayload(prepayResult.getPrepayPayload());
            return ok(resp);
        } catch (Exception e) {
            channelLog.setResponseParams(e.getMessage());
            channelLog.setStatus(ChannelLogStatusEnum.FAILED);
            payChannelLogService.save(channelLog);
            return requestFail("渠道预下单失败：" + e.getMessage());
        }
    }

    @Override
    public Result<PayOrderResponse> queryById(IdRequest request) {
        PayOrderDO order = payOrderService.getById(request.getId());
        return ok(BeanCopierUtil.copy(order, PayOrderResponse.class));
    }

    @Override
    public Result<PayOrderResponse> queryByBiz(String bizType, String bizNo) {
        PayOrderDO order = payOrderService.getByBizTypeAndBizNo(bizType, bizNo);
        return ok(BeanCopierUtil.copy(order, PayOrderResponse.class));
    }

}

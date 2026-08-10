// com.wzkris.payment.remote.impl.order.PayOrderRemoteApiImpl.java
package com.wzkris.payment.remote.impl.order;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.payment.domain.PayChannelConfigDO;
import com.wzkris.payment.domain.PayChannelLogDO;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.enums.channel.ChannelLogStatusEnum;
import com.wzkris.payment.enums.channel.ChannelStatusEnum;
import com.wzkris.payment.enums.pay.PayModeEnum;
import com.wzkris.payment.enums.pay.PayStatusEnum;
import com.wzkris.payment.impl.router.PaymentProviderRouter;
import com.wzkris.payment.properties.PaymentProperties;
import com.wzkris.payment.provider.model.ChannelResult;
import com.wzkris.payment.provider.model.PaymentProviderContext;
import com.wzkris.payment.provider.model.PrepayResult;
import com.wzkris.payment.remote.api.order.PayOrderRemoteApi;
import com.wzkris.payment.remote.api.order.request.OrderNoQueryRequest;
import com.wzkris.payment.remote.api.order.request.PayOrderCloseRequest;
import com.wzkris.payment.remote.api.order.request.PayOrderCreateRequest;
import com.wzkris.payment.remote.api.order.response.PayOrderCreateResponse;
import com.wzkris.payment.remote.api.order.response.PayOrderQueryResponse;
import com.wzkris.payment.service.PayChannelLogService;
import com.wzkris.payment.service.PayOrderService;
import com.wzkris.payment.util.OrderNoGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class PayOrderRemoteApiImpl extends AbstractApi implements PayOrderRemoteApi {

    private final PayOrderService payOrderService;

    private final PayChannelLogService payChannelLogService;

    private final PaymentProviderRouter router;

    private final OrderNoGenerator orderNoGenerator;

    private final PaymentProperties properties;

    @Override
    public Result<PayOrderCreateResponse> create(PayOrderCreateRequest request) {
        // 预校验渠道+商户配置，失败早于建单（resolve 校验配置存在/provider，渠道匹配/停用/payMode 白名单由本层校验）
        ChannelResult<PaymentProviderContext> resolved = router.resolve(request.getConfigId());
        if (!resolved.success()) {
            return requestFail(resolved.errMsg());
        }
        PaymentProviderContext ctx = resolved.data();
        PayChannelConfigDO config = ctx.config();
        if (config.getChannel() != request.getChannel()) {
            return requestFail("配置与渠道不匹配");
        }
        if (config.getStatus() != ChannelStatusEnum.ENABLED) {
            return requestFail("渠道配置已停用");
        }
        if (!config.supports(request.getPayMode())) {
            return requestFail("该渠道配置不支持此支付方式:" + request.getPayMode().getDescription());
        }
        if (request.getPayMode() == PayModeEnum.JSAPI && request.getPayerId() == null) {
            return requestFail("JSAPI支付需提供payerId(微信open_id)");
        }

        // 生成订单（不防重：重试多建的 PENDING 由过期关单 Job 兜底，不会重复扣款）
        PayOrderDO order = new PayOrderDO();
        order.setOrderNo(orderNoGenerator.nextOrderNo());
        order.setChannel(request.getChannel());
        order.setConfigId(config.getId());
        order.setPayMode(request.getPayMode());
        order.setSubject(request.getSubject());
        order.setAmount(request.getAmount());
        order.setPayerId(request.getPayerId());
        order.setClientIp(request.getClientIp());
        order.setNotifyUrl(request.getNotifyUrl());
        order.setStatus(PayStatusEnum.PENDING);
        int expireMin = request.getExpireMinutes() != null ? request.getExpireMinutes() : properties.getDefaultExpireMinutes();
        order.setExpireAt(OffsetDateTime.now().plusMinutes(expireMin));
        payOrderService.save(order);

        // 留痕
        PayChannelLogDO channelLog = new PayChannelLogDO();
        channelLog.setPayOrderId(order.getId());
        channelLog.setChannel(order.getChannel());
        channelLog.setConfigId(config.getId());
        channelLog.setPayMode(order.getPayMode());
        channelLog.setRequestParams(JsonUtil.toJsonString(order));

        ChannelResult<PrepayResult> prepayResult = ctx.provider().prepay(order, ctx);
        if (!prepayResult.success()) {
            channelLog.setResponseParams(prepayResult.errMsg());
            channelLog.setStatus(ChannelLogStatusEnum.FAILED);
            payChannelLogService.save(channelLog);
            return requestFail("渠道预下单失败：" + prepayResult.errMsg());
        }

        channelLog.setResponseParams(JsonUtil.toJsonString(prepayResult.data()));
        channelLog.setStatus(ChannelLogStatusEnum.SUCCESS);
        payChannelLogService.save(channelLog);

        // 保存渠道返回的支付信息，便于对账排查与唤起重试（订单先于 prePay 落库，此处需再更新一次）
        order.setChannelPrepayData(prepayResult.data().getPrepayPayload());
        order.setChannelPrepayTime(OffsetDateTime.now());
        payOrderService.updateById(order);

        PayOrderCreateResponse resp = new PayOrderCreateResponse();
        resp.setId(order.getId());
        resp.setOrderNo(order.getOrderNo());
        resp.setChannel(order.getChannel());
        resp.setPayMode(order.getPayMode());
        resp.setStatus(order.getStatus());
        resp.setExpireAt(order.getExpireAt());
        resp.setPrepayPayload(prepayResult.data().getPrepayPayload());
        return ok(resp);
    }

    @Override
    public Result<PayOrderQueryResponse> queryById(IdRequest request) {
        PayOrderDO order = payOrderService.getById(request.getId());
        return ok(BeanCopierUtil.copy(order, PayOrderQueryResponse.class));
    }

    @Override
    public Result<PayOrderQueryResponse> queryByOrderNo(OrderNoQueryRequest request) {
        PayOrderDO order = payOrderService.getByOrderNo(request.getOrderNo());
        return ok(BeanCopierUtil.copy(order, PayOrderQueryResponse.class));
    }

    @Override
    public Result<Void> close(PayOrderCloseRequest request) {
        PayOrderDO order;
        if (request.getId() != null) {
            order = payOrderService.getById(request.getId());
        } else if (request.getOrderNo() != null) {
            order = payOrderService.getByOrderNo(request.getOrderNo());
        } else {
            return requestFail("id与orderNo至少传一个");
        }
        if (order == null) {
            return requestFail("订单不存在");
        }
        if (order.getStatus() != PayStatusEnum.PENDING) {
            return requestFail("订单状态不允许关单");
        }

        // 配对渠道+商户配置（resolve 校验配置存在/provider，渠道由配置派生）
        ChannelResult<PaymentProviderContext> resolved = router.resolve(order.getConfigId());
        if (!resolved.success()) {
            return requestFail(resolved.errMsg());
        }
        PaymentProviderContext ctx = resolved.data();
        PayChannelConfigDO config = ctx.config();

        // 留痕
        PayChannelLogDO channelLog = new PayChannelLogDO();
        channelLog.setPayOrderId(order.getId());
        channelLog.setChannel(order.getChannel());
        channelLog.setConfigId(config.getId());
        channelLog.setPayMode(order.getPayMode());
        channelLog.setRequestParams(JsonUtil.toJsonString(order));

        ChannelResult<Void> closeResult = ctx.provider().close(order, ctx);
        if (!closeResult.success()) {
            channelLog.setResponseParams(closeResult.errMsg());
            channelLog.setStatus(ChannelLogStatusEnum.FAILED);
            payChannelLogService.save(channelLog);
            return requestFail("渠道关单失败：" + closeResult.errMsg());
        }

        channelLog.setResponseParams("CLOSED");
        channelLog.setStatus(ChannelLogStatusEnum.SUCCESS);
        payChannelLogService.save(channelLog);
        return toRes(payOrderService.updateToClosed(order.getId()));
    }

}

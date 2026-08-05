package com.wzkris.payment.impl.order;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.payment.api.order.PayOrderMngApi;
import com.wzkris.payment.api.order.request.PayOrderMngPageRequest;
import com.wzkris.payment.api.order.response.PayOrderResponse;
import com.wzkris.payment.domain.PayOrderDO;
import com.wzkris.payment.enums.pay.PayStatusEnum;
import com.wzkris.payment.provider.PaymentProviderRouter;
import com.wzkris.payment.provider.ProviderContext;
import com.wzkris.payment.service.PayOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 支付订单管理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class PayOrderMngApiImpl extends AbstractApi implements PayOrderMngApi {

    private final PayOrderService payOrderService;

    private final PaymentProviderRouter router;

    @Override
    public Result<Page<PayOrderResponse>> queryPage(PayOrderMngPageRequest request) {
        IPage<PayOrderDO> page = payOrderService.page(request.buildPage(), buildQueryWrapper(request));
        return ok(Page.of(page, BeanCopierUtil.copyList(page.getRecords(), PayOrderResponse.class)));
    }

    private LambdaQueryWrapper<PayOrderDO> buildQueryWrapper(PayOrderMngPageRequest request) {
        return new LambdaQueryWrapper<PayOrderDO>()
                .like(StringUtil.isNotEmpty(request.getOrderNo()), PayOrderDO::getOrderNo, request.getOrderNo())
                .eq(StringUtil.isNotEmpty(request.getBizType()), PayOrderDO::getBizType, request.getBizType())
                .like(StringUtil.isNotEmpty(request.getBizNo()), PayOrderDO::getBizNo, request.getBizNo())
                .eq(request.getChannel() != null, PayOrderDO::getChannel, request.getChannel())
                .eq(request.getStatus() != null, PayOrderDO::getStatus, request.getStatus())
                .orderByDesc(PayOrderDO::getId);
    }

    @Override
    public Result<PayOrderResponse> queryInfo(IdRequest request) {
        return ok(BeanCopierUtil.copy(payOrderService.getById(request.getId()), PayOrderResponse.class));
    }

    @Override
    public Result<Void> close(IdRequest request) {
        PayOrderDO order = payOrderService.getById(request.getId());
        if (order == null) {
            return requestFail("订单不存在");
        }
        if (order.getStatus() != PayStatusEnum.PENDING) {
            return requestFail("订单状态不允许关单");
        }
        // 关单沿用原成交配置
        ProviderContext ctx = router.resolve(order.getChannel(), order.getConfigId());
        ctx.getProvider().close(order, ctx.getConfig());
        return toRes(payOrderService.updateToClosed(order.getId()));
    }
}

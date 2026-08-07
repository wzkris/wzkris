package com.wzkris.payment.impl.refund;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.payment.api.refund.PayRefundMngApi;
import com.wzkris.payment.api.refund.request.PayRefundMngPageRequest;
import com.wzkris.payment.api.refund.response.RefundOrderResponse;
import com.wzkris.payment.domain.PayRefundOrderDO;
import com.wzkris.payment.service.PayRefundOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 退款订单管理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class PayRefundMngApiImpl extends AbstractApi implements PayRefundMngApi {

    private final PayRefundOrderService refundOrderService;

    @Override
    public Result<Page<RefundOrderResponse>> queryPage(PayRefundMngPageRequest request) {
        IPage<PayRefundOrderDO> page = refundOrderService.page(request.buildPage(), buildQueryWrapper(request));
        return ok(Page.of(page, BeanCopierUtil.copyList(page.getRecords(), RefundOrderResponse.class)));
    }

    private LambdaQueryWrapper<PayRefundOrderDO> buildQueryWrapper(PayRefundMngPageRequest request) {
        return new LambdaQueryWrapper<PayRefundOrderDO>()
                .like(StringUtil.isNotEmpty(request.getRefundNo()), PayRefundOrderDO::getRefundNo, request.getRefundNo())
                .eq(request.getPayOrderId() != null, PayRefundOrderDO::getPayOrderId, request.getPayOrderId())
                .eq(request.getChannel() != null, PayRefundOrderDO::getChannel, request.getChannel())
                .eq(request.getStatus() != null, PayRefundOrderDO::getStatus, request.getStatus())
                .orderByDesc(PayRefundOrderDO::getId);
    }

    @Override
    public Result<RefundOrderResponse> queryById(IdRequest request) {
        return ok(BeanCopierUtil.copy(refundOrderService.getById(request.getId()), RefundOrderResponse.class));
    }

}

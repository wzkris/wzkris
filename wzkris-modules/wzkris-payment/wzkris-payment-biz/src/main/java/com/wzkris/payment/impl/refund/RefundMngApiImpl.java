package com.wzkris.payment.impl.refund;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.payment.api.refund.RefundMngApi;
import com.wzkris.payment.api.refund.request.RefundMngPageRequest;
import com.wzkris.payment.api.refund.response.RefundOrderResponse;
import com.wzkris.payment.domain.RefundOrderDO;
import com.wzkris.payment.service.RefundOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 退款订单管理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class RefundMngApiImpl extends AbstractApi implements RefundMngApi {

    private final RefundOrderService refundOrderService;

    @Override
    public Result<Page<RefundOrderResponse>> queryPage(RefundMngPageRequest request) {
        IPage<RefundOrderDO> page = refundOrderService.page(request.buildPage(), buildQueryWrapper(request));
        return ok(Page.of(page, BeanCopierUtil.copyList(page.getRecords(), RefundOrderResponse.class)));
    }

    private LambdaQueryWrapper<RefundOrderDO> buildQueryWrapper(RefundMngPageRequest request) {
        return new LambdaQueryWrapper<RefundOrderDO>()
                .like(StringUtil.isNotEmpty(request.getRefundNo()), RefundOrderDO::getRefundNo, request.getRefundNo())
                .eq(request.getPayOrderId() != null, RefundOrderDO::getPayOrderId, request.getPayOrderId())
                .eq(request.getChannel() != null, RefundOrderDO::getChannel, request.getChannel())
                .eq(request.getStatus() != null, RefundOrderDO::getStatus, request.getStatus())
                .orderByDesc(RefundOrderDO::getId);
    }

    @Override
    public Result<RefundOrderResponse> queryById(IdRequest request) {
        return ok(BeanCopierUtil.copy(refundOrderService.getById(request.getId()), RefundOrderResponse.class));
    }

}

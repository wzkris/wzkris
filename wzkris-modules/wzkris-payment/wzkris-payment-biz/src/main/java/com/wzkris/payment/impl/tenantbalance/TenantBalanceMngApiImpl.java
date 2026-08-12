package com.wzkris.payment.impl.tenantbalance;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.payment.api.tenantbalance.TenantBalanceMngApi;
import com.wzkris.payment.api.tenantbalance.request.TenantBalanceTransactionLogMngPageRequest;
import com.wzkris.payment.api.tenantbalance.response.TenantBalanceTransactionLogMngPageResponse;
import com.wzkris.payment.domain.TenantBalanceTransactionLogDO;
import com.wzkris.payment.mapper.TenantBalanceTransactionLogMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TenantBalanceMngApiImpl extends AbstractApi implements TenantBalanceMngApi {

    private final TenantBalanceTransactionLogMapper tenantBalanceTransactionLogMapper;

    @Override
    public Result<Page<TenantBalanceTransactionLogMngPageResponse>> queryTransactionPage(TenantBalanceTransactionLogMngPageRequest request) {
        IPage<TenantBalanceTransactionLogDO> page = tenantBalanceTransactionLogMapper.selectPage(request.buildPage(), this.buildBalanceQueryWrapper(request));
        List<TenantBalanceTransactionLogMngPageResponse> list = page.getRecords().stream().map(this::toResponse).toList();
        return ok(Page.of(page, list));
    }

    private LambdaQueryWrapper<TenantBalanceTransactionLogDO> buildBalanceQueryWrapper(TenantBalanceTransactionLogMngPageRequest request) {
        return new LambdaQueryWrapper<TenantBalanceTransactionLogDO>()
                .eq(ObjectUtils.isNotEmpty(request.getTenantId()), TenantBalanceTransactionLogDO::getTenantId, request.getTenantId())
                .like(Objects.nonNull(request.getRecordType()), TenantBalanceTransactionLogDO::getRecordType, request.getRecordType())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        TenantBalanceTransactionLogDO::getCreateAt,
                        request.getBeginTime(), request.getEndTime())
                .orderByDesc(TenantBalanceTransactionLogDO::getId);
    }

    private TenantBalanceTransactionLogMngPageResponse toResponse(TenantBalanceTransactionLogDO recordDO) {
        TenantBalanceTransactionLogMngPageResponse response = new TenantBalanceTransactionLogMngPageResponse();
        response.setId(recordDO.getId());
        response.setTenantId(recordDO.getTenantId());
        response.setAmount(recordDO.getAmount());
        response.setRecordType(recordDO.getRecordType());
        response.setBizType(recordDO.getBizType());
        response.setBizNo(recordDO.getBizNo());
        response.setCreateAt(recordDO.getCreateAt());
        response.setRemark(recordDO.getRemark());
        return response;
    }

}
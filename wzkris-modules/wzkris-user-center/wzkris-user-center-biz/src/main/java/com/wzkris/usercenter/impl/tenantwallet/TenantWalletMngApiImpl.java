package com.wzkris.usercenter.impl.tenantwallet;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.tenantwallet.TenantWalletMngApi;
import com.wzkris.usercenter.api.tenantwallet.request.TenantWalletRecordMngPageRequest;
import com.wzkris.usercenter.api.tenantwallet.response.TenantWalletRecordResponse;
import com.wzkris.usercenter.domain.TenantWalletRecordDO;
import com.wzkris.usercenter.mapper.TenantWalletRecordMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TenantWalletMngApiImpl extends AbstractApi implements TenantWalletMngApi {

    private final TenantWalletRecordMapper tenantWalletRecordMapper;

    @Override
    public Result<Page<TenantWalletRecordResponse>> queryRecordPage(TenantWalletRecordMngPageRequest request) {
        IPage<TenantWalletRecordDO> page = tenantWalletRecordMapper.selectPage(request.buildPage(), this.buildWalletQueryWrapper(request));
        List<TenantWalletRecordResponse> list = page.getRecords().stream().map(this::toResponse).toList();
        return ok(Page.of(page, list));
    }

    private LambdaQueryWrapper<TenantWalletRecordDO> buildWalletQueryWrapper(TenantWalletRecordMngPageRequest request) {
        return new LambdaQueryWrapper<TenantWalletRecordDO>()
                .eq(ObjectUtils.isNotEmpty(request.getTenantId()), TenantWalletRecordDO::getTenantId, request.getTenantId())
                .like(Objects.nonNull(request.getRecordType()), TenantWalletRecordDO::getRecordType, request.getRecordType())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        TenantWalletRecordDO::getCreateAt,
                        request.getBeginTime(), request.getEndTime())
                .orderByDesc(TenantWalletRecordDO::getRecordId);
    }

    private TenantWalletRecordResponse toResponse(TenantWalletRecordDO recordDO) {
        TenantWalletRecordResponse response = new TenantWalletRecordResponse();
        response.setRecordId(recordDO.getRecordId());
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

package com.wzkris.usercenter.impl.tenantwallet;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.usercenter.api.tenantwallet.TenantWalletMngApi;
import com.wzkris.usercenter.domain.TenantWalletRecordDO;
import com.wzkris.usercenter.mapper.TenantWalletRecordMapper;
import com.wzkris.usercenter.request.tenantwallet.TenantWalletRecordQueryRequest;
import com.wzkris.usercenter.response.tenantwallet.TenantWalletRecordResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantWalletMngApiImpl implements TenantWalletMngApi {

    private final TenantWalletRecordMapper tenantWalletRecordMapper;

    @Override
    public List<TenantWalletRecordResponse> listRecord(TenantWalletRecordQueryRequest request) {
        return tenantWalletRecordMapper.selectList(this.buildWalletQueryWrapper(request)).stream()
                .map(this::toResponse)
                .toList();
    }

    private LambdaQueryWrapper<TenantWalletRecordDO> buildWalletQueryWrapper(TenantWalletRecordQueryRequest request) {
        return new LambdaQueryWrapper<TenantWalletRecordDO>()
                .eq(
                        ObjectUtils.isNotEmpty(request.getTenantId()),
                        TenantWalletRecordDO::getTenantId,
                        request.getTenantId())
                .like(
                        StringUtil.isNotBlank(request.getRecordType()),
                        TenantWalletRecordDO::getRecordType,
                        request.getRecordType())
                .between(
                        request.getParam("beginTime") != null && request.getParam("endTime") != null,
                        TenantWalletRecordDO::getCreateAt,
                        request.getParam("beginTime"),
                        request.getParam("endTime"))
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

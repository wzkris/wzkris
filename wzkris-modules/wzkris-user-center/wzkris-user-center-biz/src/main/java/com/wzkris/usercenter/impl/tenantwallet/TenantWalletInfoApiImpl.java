package com.wzkris.usercenter.impl.tenantwallet;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.tenantwallet.TenantWalletInfoApi;
import com.wzkris.usercenter.api.tenantwallet.request.TenantWalletRecordInfoPageRequest;
import com.wzkris.usercenter.api.tenantwallet.request.WalletWithdrawalRequest;
import com.wzkris.usercenter.api.tenantwallet.response.TenantWalletInfoQueryResponse;
import com.wzkris.usercenter.api.tenantwallet.response.TenantWalletRecordInfoPageResponse;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.domain.TenantWalletRecordDO;
import com.wzkris.usercenter.mapper.TenantWalletRecordMapper;
import com.wzkris.usercenter.service.TenantInfoService;
import com.wzkris.usercenter.service.TenantWalletInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TenantWalletInfoApiImpl extends AbstractApi implements TenantWalletInfoApi {

    private final TenantInfoService tenantInfoService;

    private final TenantWalletInfoService tenantWalletInfoService;

    private final TenantWalletRecordMapper tenantWalletRecordMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Result<TenantWalletInfoQueryResponse> query() {
        return ok(tenantWalletInfoService.getById2VO(SecurityUtil.getTenantId(), TenantWalletInfoQueryResponse.class));
    }

    @Override
    public Result<Page<TenantWalletRecordInfoPageResponse>> queryRecordPage(TenantWalletRecordInfoPageRequest request) {
        IPage<TenantWalletRecordDO> page = tenantWalletRecordMapper.selectPage(request.buildPage(), this.buildWalletQueryWrapper(request));
        List<TenantWalletRecordInfoPageResponse> list = page.getRecords().stream().map(this::toResponse).toList();
        return ok(Page.of(page, list));
    }

    @Override
    public Result<Void> withdrawal(WalletWithdrawalRequest request) {
        TenantInfoDO tenantInfoDO = tenantInfoService.getById(SecurityUtil.getTenantId());
        if (!passwordEncoder.matches(request.getOperPwd(), tenantInfoDO.getOperPwd())) {
            return Result.requestFail("密码错误");
        }
        return Result.ok();
    }

    private LambdaQueryWrapper<TenantWalletRecordDO> buildWalletQueryWrapper(TenantWalletRecordInfoPageRequest request) {
        return new LambdaQueryWrapper<TenantWalletRecordDO>()
                .eq(TenantWalletRecordDO::getTenantId, SecurityUtil.getTenantId())
                .like(Objects.nonNull(request.getRecordType()), TenantWalletRecordDO::getRecordType, request.getRecordType())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        TenantWalletRecordDO::getCreateAt,
                        request.getBeginTime(), request.getEndTime())
                .orderByDesc(TenantWalletRecordDO::getId);
    }

    private TenantWalletRecordInfoPageResponse toResponse(TenantWalletRecordDO recordDO) {
        TenantWalletRecordInfoPageResponse response = new TenantWalletRecordInfoPageResponse();
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

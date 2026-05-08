package com.wzkris.usercenter.impl.tenantwallet;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.model.TenantLoginUser;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.usercenter.api.tenantwallet.TenantWalletInfoApi;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.domain.TenantWalletRecordDO;
import com.wzkris.usercenter.mapper.TenantInfoMapper;
import com.wzkris.usercenter.mapper.TenantWalletInfoMapper;
import com.wzkris.usercenter.mapper.TenantWalletRecordMapper;
import com.wzkris.usercenter.request.tenantwallet.TenantWalletRecordInfoPageRequest;
import com.wzkris.usercenter.request.tenantwallet.WalletWithdrawalRequest;
import com.wzkris.usercenter.response.tenantwallet.TenantWalletInfoResponse;
import com.wzkris.usercenter.response.tenantwallet.TenantWalletRecordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TenantWalletInfoApiImpl extends AbstractApi implements TenantWalletInfoApi {

    private final TenantInfoMapper tenantInfoMapper;

    private final TenantWalletInfoMapper tenantWalletInfoMapper;

    private final TenantWalletRecordMapper tenantWalletRecordMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Result<TenantWalletInfoResponse> queryInfo() {
        TenantLoginUser loginUser = SecurityUtil.getLoginUser(TenantLoginUser.class);
        return ok(tenantWalletInfoMapper.selectById2VO(loginUser.getTenantId(), TenantWalletInfoResponse.class));
    }

    @Override
    public Result<Page<TenantWalletRecordResponse>> queryRecordPage(TenantWalletRecordInfoPageRequest request) {
        startPage(request);
        List<TenantWalletRecordResponse> list = tenantWalletRecordMapper.selectList(this.buildWalletQueryWrapper(request))
                .stream()
                .map(this::toResponse)
                .toList();
        return getPageResult(list);
    }

    @Override
    public Result<Void> withdrawal(WalletWithdrawalRequest request) {
        TenantLoginUser loginUser = SecurityUtil.getLoginUser(TenantLoginUser.class);
        TenantInfoDO tenantInfoDO = tenantInfoMapper.selectById(loginUser.getTenantId());
        if (!passwordEncoder.matches(request.getOperPwd(), tenantInfoDO.getOperPwd())) {
            return Result.requestFail("密码错误");
        }
        return Result.ok();
    }

    private LambdaQueryWrapper<TenantWalletRecordDO> buildWalletQueryWrapper(TenantWalletRecordInfoPageRequest request) {
        TenantLoginUser loginUser = SecurityUtil.getLoginUser(TenantLoginUser.class);
        return new LambdaQueryWrapper<TenantWalletRecordDO>()
                .eq(TenantWalletRecordDO::getTenantId, loginUser.getTenantId())
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

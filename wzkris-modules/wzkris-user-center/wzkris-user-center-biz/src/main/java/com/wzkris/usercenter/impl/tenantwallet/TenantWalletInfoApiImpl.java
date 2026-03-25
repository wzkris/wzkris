package com.wzkris.usercenter.impl.tenantwallet;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.model.TenantLoginUser;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.usercenter.api.tenantwallet.TenantWalletInfoApi;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.domain.TenantWalletRecordDO;
import com.wzkris.usercenter.mapper.TenantInfoMapper;
import com.wzkris.usercenter.mapper.TenantWalletInfoMapper;
import com.wzkris.usercenter.mapper.TenantWalletRecordMapper;
import com.wzkris.usercenter.request.tenantwallet.TenantWalletRecordQueryRequest;
import com.wzkris.usercenter.request.tenantwallet.WalletWithdrawalRequest;
import com.wzkris.usercenter.response.tenantwallet.TenantWalletInfoResponse;
import com.wzkris.usercenter.response.tenantwallet.TenantWalletRecordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantWalletInfoApiImpl extends BaseController implements TenantWalletInfoApi {

    private final TenantInfoMapper tenantInfoMapper;

    private final TenantWalletInfoMapper tenantWalletInfoMapper;

    private final TenantWalletRecordMapper tenantWalletRecordMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public TenantWalletInfoResponse queryInfo() {
        Long tenantId = SecurityUtil.getLoginUser(TenantLoginUser.class).getTenantId();
        return tenantWalletInfoMapper.selectById2VO(tenantId, TenantWalletInfoResponse.class);
    }

    @Override
    public Result<Page<TenantWalletRecordResponse>> queryRecordPage(TenantWalletRecordQueryRequest request) {
        startPage();
        List<TenantWalletRecordResponse> list = tenantWalletRecordMapper.selectList(this.buildWalletQueryWrapper(request)).stream()
                .map(this::toResponse)
                .toList();
        return getDataTable(list);
    }

    @Override
    public Result<Void> withdrawal(WalletWithdrawalRequest request) {
        Long tenantId = SecurityUtil.getLoginUser(TenantLoginUser.class).getTenantId();
        TenantInfoDO tenantInfoDO = tenantInfoMapper.selectById(tenantId);
        if (!passwordEncoder.matches(request.getOperPwd(), tenantInfoDO.getOperPwd())) {
            return Result.requestFail("密码错误");
        }
        return Result.ok();
    }

    private LambdaQueryWrapper<TenantWalletRecordDO> buildWalletQueryWrapper(TenantWalletRecordQueryRequest request) {
        return new LambdaQueryWrapper<TenantWalletRecordDO>()
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

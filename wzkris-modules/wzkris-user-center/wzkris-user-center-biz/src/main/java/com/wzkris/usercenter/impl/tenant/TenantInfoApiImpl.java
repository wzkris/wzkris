package com.wzkris.usercenter.impl.tenant;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.security.model.TenantLoginUser;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.tenant.TenantInfoApi;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.mapper.AdminInfoMapper;
import com.wzkris.usercenter.mapper.PostInfoMapper;
import com.wzkris.usercenter.mapper.TenantInfoMapper;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import com.wzkris.usercenter.request.tenant.TenantInfoBasicUpdateRequest;
import com.wzkris.usercenter.response.tenant.TenantInfoResponse;
import com.wzkris.usercenter.response.tenant.TenantUsedQuotaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantInfoApiImpl extends BaseController implements TenantInfoApi {

    private final AdminInfoMapper adminInfoMapper;

    private final PostInfoMapper postInfoMapper;

    private final TenantInfoMapper tenantInfoMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Result<TenantInfoResponse> queryInfo() {
        Long tenantId = SecurityUtil.getLoginUser(TenantLoginUser.class).getTenantId();
        return ok(tenantInfoMapper.selectVOById(tenantId));
    }

    @Override
    public Result<Void> updateBasicInfo(TenantInfoBasicUpdateRequest request) {
        Long tenantId = SecurityUtil.getLoginUser(TenantLoginUser.class).getTenantId();
        TenantInfoDO tenantInfoDO = BeanUtil.convert(request, new TenantInfoDO(tenantId));
        return toRes(tenantInfoMapper.updateById(tenantInfoDO));
    }

    @Override
    public Result<TenantUsedQuotaResponse> queryLimitInfo() {
        TenantUsedQuotaResponse usedQuotaVO = new TenantUsedQuotaResponse();
        usedQuotaVO.setAccountHas(Math.toIntExact(adminInfoMapper.selectCount(null)));
        usedQuotaVO.setPostHas(Math.toIntExact(postInfoMapper.selectCount(null)));
        return ok(usedQuotaVO);
    }

    @Override
    public Result<Void> updateOperPwd(PasswordUpdateRequest request) {
        Long tenantId = SecurityUtil.getLoginUser(TenantLoginUser.class).getTenantId();
        String operPwd = tenantInfoMapper.selectOperPwdById(tenantId);
        if (!passwordEncoder.matches(request.getOldPassword(), operPwd)) {
            return requestFail("修改密码失败，旧密码错误");
        }
        if (passwordEncoder.matches(request.getNewPassword(), operPwd)) {
            return requestFail("新密码不能与旧密码相同");
        }
        TenantInfoDO update = new TenantInfoDO(tenantId);
        update.setOperPwd(passwordEncoder.encode(request.getNewPassword()));
        return toRes(tenantInfoMapper.updateById(update));
    }

}

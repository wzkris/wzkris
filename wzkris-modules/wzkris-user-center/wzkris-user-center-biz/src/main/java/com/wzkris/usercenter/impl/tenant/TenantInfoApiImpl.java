package com.wzkris.usercenter.impl.tenant;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.tenant.TenantInfoApi;
import com.wzkris.usercenter.api.tenant.request.TenantInfoBasicUpdateRequest;
import com.wzkris.usercenter.api.tenant.response.TenantInfoQueryResponse;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.mapper.TenantInfoMapper;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import com.wzkris.usercenter.service.TenantInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantInfoApiImpl extends AbstractApi implements TenantInfoApi {

    private final TenantInfoMapper tenantInfoMapper;

    private final TenantInfoService tenantInfoService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Result<TenantInfoQueryResponse> queryInfo() {
        return ok(tenantInfoMapper.selectVOById(SecurityUtil.getTenantId()));
    }

    @Override
    public Result<Void> updateBasicInfo(TenantInfoBasicUpdateRequest request) {
        TenantInfoDO tenantInfoDO = BeanCopierUtil.copy(request, new TenantInfoDO(SecurityUtil.getTenantId()));
        return toRes(tenantInfoService.updateById(tenantInfoDO));
    }

    @Override
    public Result<Void> updateOperPwd(PasswordUpdateRequest request) {
        Long tenantId = SecurityUtil.getTenantId();
        String operPwd = tenantInfoService.getObjByObj(TenantInfoDO::getOperPwd,
                TenantInfoDO::getId, tenantId);
        if (!passwordEncoder.matches(request.getOldPassword(), operPwd)) {
            return requestFail("修改密码失败，旧密码错误");
        }
        if (passwordEncoder.matches(request.getNewPassword(), operPwd)) {
            return requestFail("新密码不能与旧密码相同");
        }
        TenantInfoDO update = new TenantInfoDO(tenantId);
        update.setOperPwd(passwordEncoder.encode(request.getNewPassword()));
        return toRes(tenantInfoService.updateById(update));
    }

}

package com.wzkris.usercenter.impl.tenant;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.security.model.LoginTenantUser;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.tenant.TenantInfoApi;
import com.wzkris.usercenter.api.tenant.request.TenantInfoBasicUpdateRequest;
import com.wzkris.usercenter.api.tenant.response.TenantInfoResponse;
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
    public Result<TenantInfoResponse> queryInfo() {
        LoginTenantUser tenantUser = SecurityUtil.getLoginUser(LoginTenantUser.class);
        return ok(tenantInfoMapper.selectVOById(tenantUser.getTenantId()));
    }

    @Override
    public Result<Void> updateBasicInfo(TenantInfoBasicUpdateRequest request) {
        LoginTenantUser tenantUser = SecurityUtil.getLoginUser(LoginTenantUser.class);
        TenantInfoDO tenantInfoDO = BeanUtil.convert(request, new TenantInfoDO(tenantUser.getTenantId()));
        return toRes(tenantInfoService.updateById(tenantInfoDO));
    }

    @Override
    public Result<Void> updateOperPwd(PasswordUpdateRequest request) {
        LoginTenantUser tenantUser = SecurityUtil.getLoginUser(LoginTenantUser.class);
        String operPwd = tenantInfoMapper.selectOneFieldByField(
                TenantInfoDO::getTenantId, tenantUser.getTenantId(), TenantInfoDO::getOperPwd);
        if (!passwordEncoder.matches(request.getOldPassword(), operPwd)) {
            return requestFail("修改密码失败，旧密码错误");
        }
        if (passwordEncoder.matches(request.getNewPassword(), operPwd)) {
            return requestFail("新密码不能与旧密码相同");
        }
        TenantInfoDO update = new TenantInfoDO(tenantUser.getTenantId());
        update.setOperPwd(passwordEncoder.encode(request.getNewPassword()));
        return toRes(tenantInfoService.updateById(update));
    }

}

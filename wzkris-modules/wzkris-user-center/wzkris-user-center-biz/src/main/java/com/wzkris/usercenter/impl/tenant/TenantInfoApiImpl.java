package com.wzkris.usercenter.impl.tenant;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.security.model.TenantLoginUser;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.tenant.TenantInfoApi;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.mapper.TenantInfoMapper;
import com.wzkris.usercenter.request.common.PasswordUpdateRequest;
import com.wzkris.usercenter.request.tenant.TenantInfoBasicUpdateRequest;
import com.wzkris.usercenter.response.tenant.TenantInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantInfoApiImpl extends AbstractApi implements TenantInfoApi {

    private final TenantInfoMapper tenantInfoMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Result<TenantInfoResponse> queryInfo() {
        TenantLoginUser loginUser = SecurityUtil.getLoginUser(TenantLoginUser.class);
        return ok(tenantInfoMapper.selectVOById(loginUser.getTenantId()));
    }

    @Override
    public Result<Void> updateBasicInfo(TenantInfoBasicUpdateRequest request) {
        TenantLoginUser loginUser = SecurityUtil.getLoginUser(TenantLoginUser.class);
        TenantInfoDO tenantInfoDO = BeanUtil.convert(request, new TenantInfoDO(loginUser.getTenantId()));
        return toRes(tenantInfoMapper.updateById(tenantInfoDO));
    }

    @Override
    public Result<Void> updateOperPwd(PasswordUpdateRequest request) {
        TenantLoginUser loginUser = SecurityUtil.getLoginUser(TenantLoginUser.class);
        String operPwd = tenantInfoMapper.selectOperPwdById(loginUser.getTenantId());
        if (!passwordEncoder.matches(request.getOldPassword(), operPwd)) {
            return requestFail("修改密码失败，旧密码错误");
        }
        if (passwordEncoder.matches(request.getNewPassword(), operPwd)) {
            return requestFail("新密码不能与旧密码相同");
        }
        TenantInfoDO update = new TenantInfoDO(loginUser.getTenantId());
        update.setOperPwd(passwordEncoder.encode(request.getNewPassword()));
        return toRes(tenantInfoMapper.updateById(update));
    }

}

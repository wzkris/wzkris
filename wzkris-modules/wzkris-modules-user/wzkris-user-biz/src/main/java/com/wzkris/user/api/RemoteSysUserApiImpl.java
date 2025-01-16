package com.wzkris.user.api;

import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.openfeign.annotation.InnerAuth;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.api.domain.request.LoginInfoReq;
import com.wzkris.user.api.domain.request.QueryPermsReq;
import com.wzkris.user.api.domain.response.SysPermissionResp;
import com.wzkris.user.api.domain.response.SysUserResp;
import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.domain.SysTenantPackage;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.mapper.SysTenantMapper;
import com.wzkris.user.mapper.SysTenantPackageMapper;
import com.wzkris.user.mapper.SysUserMapper;
import com.wzkris.user.service.SysPermissionService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 内部系统用户接口
 * @date : 2024/4/15 16:20
 */
@Hidden
@InnerAuth
@RestController
@RequiredArgsConstructor
public class RemoteSysUserApiImpl extends BaseController implements RemoteSysUserApi {

    private final SysUserMapper userMapper;

    private final SysTenantMapper tenantMapper;

    private final SysTenantPackageMapper tenantPackageMapper;

    private final SysPermissionService sysPermissionService;

    @Override
    public Result<SysUserResp> getByUsername(String username) {
        SysUser user = userMapper.selectByUsername(username);
        SysUserResp userResp = BeanUtil.convert(user, SysUserResp.class);
        this.retrieveAllStatus(userResp);
        return ok(userResp);
    }

    @Override
    public Result<SysUserResp> getByPhoneNumber(String phoneNumber) {
        SysUser sysUser = userMapper.selectByPhoneNumber(phoneNumber);
        SysUserResp userResp = BeanUtil.convert(sysUser, SysUserResp.class);
        this.retrieveAllStatus(userResp);
        return ok(userResp);
    }

    /**
     * 查询状态
     */
    private void retrieveAllStatus(@Nullable SysUserResp userResp) {
        if (userResp == null) return;
        if (SysTenant.isSuperTenant(userResp.getTenantId())) {
            userResp.setTenantStatus(CommonConstants.STATUS_ENABLE);
            userResp.setTenantExpired(CommonConstants.NOT_EXPIRED_TIME);
            userResp.setPackageStatus(CommonConstants.STATUS_ENABLE);
        } else {
            SysTenant tenant = tenantMapper.selectById(userResp.getTenantId());
            userResp.setTenantStatus(tenant.getStatus());
            userResp.setTenantExpired(tenant.getExpireTime());
            SysTenantPackage tenantPackage = tenantPackageMapper.selectById(tenant.getPackageId());
            userResp.setPackageStatus(tenantPackage.getStatus());
        }
    }

    @Override
    public Result<SysPermissionResp> getPermission(QueryPermsReq queryPermsReq) {
        SysPermissionResp permission = sysPermissionService
                .getPermission(queryPermsReq.getUserId(), queryPermsReq.getTenantId(), queryPermsReq.getDeptId());
        return ok(permission);
    }

    @Override
    public void updateLoginInfo(LoginInfoReq loginInfoReq) {
        SysUser sysUser = new SysUser(loginInfoReq.getUserId());
        sysUser.setLoginIp(loginInfoReq.getLoginIp());
        sysUser.setLoginDate(loginInfoReq.getLoginDate());

        userMapper.updateById(sysUser);
    }

}

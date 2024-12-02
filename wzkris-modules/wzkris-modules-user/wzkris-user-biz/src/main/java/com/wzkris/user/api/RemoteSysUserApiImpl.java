package com.wzkris.user.api;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.MapstructUtil;
import com.wzkris.common.openfeign.annotation.InnerAuth;
import com.wzkris.user.api.domain.request.LoginInfoReq;
import com.wzkris.user.api.domain.request.QueryPermsReq;
import com.wzkris.user.api.domain.response.SysPermissionResp;
import com.wzkris.user.api.domain.response.SysUserResp;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.mapper.SysUserMapper;
import com.wzkris.user.service.SysPermissionService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import static com.wzkris.common.core.domain.Result.ok;


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
public class RemoteSysUserApiImpl implements RemoteSysUserApi {
    private final SysUserMapper userMapper;
    private final SysPermissionService sysPermissionService;

    @Override
    public Result<SysUserResp> getByUsername(String username) {
        SysUser sysUser = userMapper.selectByUsername(username);
        SysUserResp sysUserResp = MapstructUtil.convert(sysUser, SysUserResp.class);
        return ok(sysUserResp);
    }

    @Override
    public Result<SysUserResp> getByPhoneNumber(String phoneNumber) {
        SysUser sysUser = userMapper.selectByPhoneNumber(phoneNumber);
        SysUserResp sysUserResp = MapstructUtil.convert(sysUser, SysUserResp.class);
        return ok(sysUserResp);
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

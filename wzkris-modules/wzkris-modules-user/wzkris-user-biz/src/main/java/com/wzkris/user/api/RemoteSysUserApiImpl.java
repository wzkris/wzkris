package com.wzkris.user.api;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.MapstructUtil;
import com.wzkris.common.openfeign.annotation.InnerAuth;
import com.wzkris.user.api.domain.dto.LoginInfoDTO;
import com.wzkris.user.api.domain.dto.QueryPermsDTO;
import com.wzkris.user.api.domain.dto.SysPermissionDTO;
import com.wzkris.user.api.domain.dto.SysUserDTO;
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
    public Result<SysUserDTO> getByUsername(String username) {
        SysUser sysUser = userMapper.selectByUsername(username);
        SysUserDTO sysUserDTO = MapstructUtil.convert(sysUser, SysUserDTO.class);
        return ok(sysUserDTO);
    }

    @Override
    public Result<SysUserDTO> getByPhoneNumber(String phoneNumber) {
        SysUser sysUser = userMapper.selectByPhoneNumber(phoneNumber);
        SysUserDTO sysUserDTO = MapstructUtil.convert(sysUser, SysUserDTO.class);
        return ok(sysUserDTO);
    }

    @Override
    public Result<SysPermissionDTO> getPermission(QueryPermsDTO queryPermsDTO) {
        SysPermissionDTO permission = sysPermissionService
                .getPermission(queryPermsDTO.getUserId(), queryPermsDTO.getTenantId(), queryPermsDTO.getDeptId());
        return ok(permission);
    }

    @Override
    public void updateLoginInfo(LoginInfoDTO loginInfoDTO) {
        SysUser sysUser = new SysUser(loginInfoDTO.getUserId());
        sysUser.setLoginIp(loginInfoDTO.getLoginIp());
        sysUser.setLoginDate(loginInfoDTO.getLoginDate());

        userMapper.updateById(sysUser);
    }

}

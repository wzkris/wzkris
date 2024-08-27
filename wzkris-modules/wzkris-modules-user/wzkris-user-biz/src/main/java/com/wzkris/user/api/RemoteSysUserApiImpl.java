package com.wzkris.user.api;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.MapstructUtil;
import com.wzkris.common.security.annotation.InnerAuth;
import com.wzkris.user.api.domain.dto.LoginInfoDTO;
import com.wzkris.user.api.domain.dto.SysPermissionDTO;
import com.wzkris.user.api.domain.dto.SysUserDTO;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.mapper.SysUserMapper;
import com.wzkris.user.service.SysMenuService;
import com.wzkris.user.service.SysPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import static com.wzkris.common.core.domain.Result.success;


/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 内部系统用户接口
 * @date : 2024/4/15 16:20
 */
@InnerAuth
@RestController
@RequiredArgsConstructor
public class RemoteSysUserApiImpl implements RemoteSysUserApi {
    private final SysUserMapper sysUserMapper;
    private final SysPermissionService sysPermissionService;
    private final SysMenuService sysMenuService;

    /**
     * 根据用户名查询系统用户
     */
    @Override
    public Result<SysUserDTO> getByUsername(String username) {
        SysUser sysUser = sysUserMapper.selectByUsername(username);
        SysUserDTO sysUserDTO = MapstructUtil.convert(sysUser, SysUserDTO.class);
        return success(sysUserDTO);
    }

    /**
     * 查询管理员权限
     */
    @Override
    public Result<SysPermissionDTO> getPermission(Long userId, Long tenantId, Long deptId) {
        SysPermissionDTO permission = sysPermissionService.getPermission(userId, tenantId, deptId);
        return success(permission);
    }

    /**
     * 更新用户登录信息
     */
    @Override
    public void updateLoginInfo(LoginInfoDTO loginInfoDTO) {
        SysUser sysUser = new SysUser(loginInfoDTO.getUserId());
        sysUser.setLoginIp(loginInfoDTO.getLoginIp());
        sysUser.setLoginDate(loginInfoDTO.getLoginDate());

        sysUserMapper.updateById(sysUser);
    }

}

package com.thingslink.user.api_feign;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.security.annotation.InnerAuth;
import com.thingslink.user.api.RemoteSysUserApi;
import com.thingslink.user.api.domain.dto.LoginInfoDTO;
import com.thingslink.user.api.domain.dto.SysPermissionDTO;
import com.thingslink.user.api.domain.dto.SysUserDTO;
import com.thingslink.user.api.domain.vo.RouterVO;
import com.thingslink.user.domain.SysUser;
import com.thingslink.user.mapper.SysUserMapper;
import com.thingslink.user.service.PermissionService;
import com.thingslink.user.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.thingslink.common.core.domain.Result.success;


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
    private final PermissionService permissionService;
    private final SysMenuService sysMenuService;

    /**
     * 根据用户名查询系统用户
     */
    @Override
    public Result<SysUserDTO> getByUsername(String username) {
        SysUser sysUser = sysUserMapper.selectByUsername(username);

        SysUserDTO sysUserDTO = new SysUserDTO();
        sysUserDTO.setUserId(sysUser.getUserId());
        sysUserDTO.setTenantId(sysUser.getTenantId());
        sysUserDTO.setDeptId(sysUser.getDeptId());
        sysUserDTO.setUsername(sysUser.getUsername());
        sysUserDTO.setNickname(sysUser.getNickname());
        sysUserDTO.setEmail(sysUser.getEmail());
        sysUserDTO.setPhoneNumber(sysUser.getPhoneNumber());
        sysUserDTO.setStatus(sysUser.getStatus());
        sysUserDTO.setGender(sysUser.getGender());
        sysUserDTO.setAvatar(sysUser.getAvatar());
        sysUserDTO.setPassword(sysUser.getPassword());
        return success(sysUserDTO);
    }

    /**
     * 查询管理员权限
     */
    @Override
    public Result<SysPermissionDTO> getPermission(Long userId, Long tenantId, Long deptId) {
        SysPermissionDTO permission = permissionService.getPermission(userId, tenantId, deptId);
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

    /**
     * 获取系统用户前端路由
     */
    @Override
    public Result<List<RouterVO>> getRouter(Long userId) {
        List<RouterVO> routerVOS = sysMenuService.listRouteTree(userId);
        return success(routerVOS);
    }
}

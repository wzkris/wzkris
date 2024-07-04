package com.wzkris.auth.oauth2.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.wzkris.auth.oauth2.model.UserModel;
import com.wzkris.auth.oauth2.service.UserDetailsServicePlus;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.security.oauth2.domain.model.LoginSyser;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.user.api.RemoteSysUserApi;
import com.wzkris.user.api.domain.dto.SysPermissionDTO;
import com.wzkris.user.api.domain.dto.SysUserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 查询系统用户信息
 * @date : 2024/2/26 10:03
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysUserDetailsService implements UserDetailsServicePlus {
    private final RemoteSysUserApi remoteSysUserApi;

    @Override
    public UserModel loadUserByUsername(String username) throws UsernameNotFoundException {
        Result<SysUserDTO> result = remoteSysUserApi.getByUsername(username);
        SysUserDTO sysUserDTO = result.checkData();
        return this.checkAndBuild(sysUserDTO);
    }

    /**
     * 构建登录用户
     */
    private UserModel checkAndBuild(SysUserDTO sysUserDTO) {
        // 校验用户状态
        this.checkAccount(sysUserDTO);

        // 获取权限信息
        Result<SysPermissionDTO> permissionDTOResult = remoteSysUserApi.getPermission(sysUserDTO.getUserId(), sysUserDTO.getDeptId());
        SysPermissionDTO permissions = permissionDTOResult.checkData();

        LoginSyser loginSyser = new LoginSyser();
        loginSyser.setUserId(sysUserDTO.getUserId());
        loginSyser.setDeptId(sysUserDTO.getDeptId());
        loginSyser.setTenantId(sysUserDTO.getTenantId());
        loginSyser.setUsername(sysUserDTO.getUsername());
        loginSyser.setPassword(sysUserDTO.getPassword());
        loginSyser.setIsAdmin(permissions.getIsAdmin());
        loginSyser.setDeptScopes(permissions.getDeptScopes());

        return new UserModel(sysUserDTO.getUsername(),
                sysUserDTO.getPassword(), permissions.getGrantedAuthority(), loginSyser);
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(SysUserDTO sysUserDTO) {
        if (sysUserDTO == null) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");// 不能明说账号不存在
        }
        if (ObjUtil.equals(sysUserDTO.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }
}

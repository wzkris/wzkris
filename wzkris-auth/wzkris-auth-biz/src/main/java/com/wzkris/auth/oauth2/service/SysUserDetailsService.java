package com.wzkris.auth.oauth2.service;

import cn.hutool.core.util.ObjUtil;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.security.oauth2.domain.WzUser;
import com.wzkris.common.security.oauth2.domain.model.LoginSyser;
import com.wzkris.common.security.oauth2.enums.UserType;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.user.api.RemoteSysUserApi;
import com.wzkris.user.api.domain.request.QueryPermsReq;
import com.wzkris.user.api.domain.response.SysPermissionResp;
import com.wzkris.user.api.domain.response.SysUserResp;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SysUserDetailsService implements UserDetailsServiceExt {
    private final RemoteSysUserApi remoteSysUserApi;

    @Override
    public WzUser loadUserByUsername(String username) throws UsernameNotFoundException {
        Result<SysUserResp> result = remoteSysUserApi.getByUsername(username);
        SysUserResp userDTO = result.checkData();
        return this.checkAndBuild(userDTO);
    }

    @Override
    public WzUser loadUserByPhoneNumber(String phoneNumber) {
        Result<SysUserResp> result = remoteSysUserApi.getByPhoneNumber(phoneNumber);
        SysUserResp userDTO = result.checkData();
        return userDTO == null ? null : this.checkAndBuild(userDTO);
    }

    /**
     * 构建登录用户
     */
    private WzUser checkAndBuild(@Nonnull SysUserResp userDTO) {
        // 校验用户状态
        this.checkAccount(userDTO);

        // 获取权限信息
        Result<SysPermissionResp> permissionDTOResult = remoteSysUserApi
                .getPermission(new QueryPermsReq(userDTO.getUserId(), userDTO.getTenantId(), userDTO.getDeptId()));
        SysPermissionResp permissions = permissionDTOResult.checkData();

        LoginSyser loginSyser = new LoginSyser();
        loginSyser.setUserId(userDTO.getUserId());
        loginSyser.setUsername(userDTO.getUsername());
        loginSyser.setTenantId(userDTO.getTenantId());
        loginSyser.setAdministrator(permissions.getAdministrator());
        loginSyser.setDeptScopes(permissions.getDeptScopes());

        return new WzUser(UserType.SYS_USER, loginSyser.getUsername(),
                loginSyser, userDTO.getPassword(), permissions.getGrantedAuthority());
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(SysUserResp userDTO) {
        if (ObjUtil.equals(userDTO.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }
}

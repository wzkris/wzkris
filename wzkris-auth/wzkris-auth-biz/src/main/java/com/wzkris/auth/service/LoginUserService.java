package com.wzkris.auth.service;

import cn.hutool.core.util.ObjUtil;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.security.oauth2.domain.model.LoginUser;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.user.api.RemoteSysUserApi;
import com.wzkris.user.api.domain.request.QueryPermsReq;
import com.wzkris.user.api.domain.response.SysPermissionResp;
import com.wzkris.user.api.domain.response.SysUserResp;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class LoginUserService {

    private final RemoteSysUserApi remoteSysUserApi;

    @Nullable
    public LoginUser getUserByPhoneNumber(String phoneNumber) {
        Result<SysUserResp> result = remoteSysUserApi.getByPhoneNumber(phoneNumber);
        SysUserResp userResp = result.checkData();
        return userResp == null ? null : this.checkAndBuild(userResp);
    }

    @Nullable
    public LoginUser getByUsernameAndPassword(String username, String password) throws UsernameNotFoundException {
        Result<SysUserResp> result = remoteSysUserApi.getByUsername(username, password);
        SysUserResp userResp = result.checkData();
        return userResp == null ? null : this.checkAndBuild(userResp);
    }

    @Nullable
    public LoginUser getUserByWechat(String channel, String wxCode) {
        return null;
    }

    /**
     * 构建登录用户
     */
    private LoginUser checkAndBuild(@Nonnull SysUserResp userResp) {
        // 校验用户状态
        this.checkAccount(userResp);

        // 获取权限信息
        Result<SysPermissionResp> permissionDTOResult = remoteSysUserApi
                .getPermission(new QueryPermsReq(userResp.getUserId(), userResp.getTenantId(), userResp.getDeptId()));
        SysPermissionResp permissions = permissionDTOResult.checkData();

        LoginUser loginUser = new LoginUser(new HashSet<>(permissions.getGrantedAuthority()));
        loginUser.setAdmin(permissions.getAdmin());
        loginUser.setUserId(userResp.getUserId());
        loginUser.setUsername(userResp.getUsername());
        loginUser.setTenantId(userResp.getTenantId());
        loginUser.setDeptScopes(permissions.getDeptScopes());

        return loginUser;
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(SysUserResp userResp) {
        if (ObjUtil.equals(userResp.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
        else if (ObjUtil.equals(userResp.getTenantStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.tenant.disabled");
        }
        else if (userResp.getTenantExpired() != CommonConstants.NOT_EXPIRED_TIME
                && userResp.getTenantExpired() < System.currentTimeMillis()) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.tenant.expired");
        }
        else if (ObjUtil.equals(userResp.getPackageStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.package.disabled");
        }
    }

}

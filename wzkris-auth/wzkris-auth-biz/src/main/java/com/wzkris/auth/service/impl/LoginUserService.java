package com.wzkris.auth.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.security.oauth2.constants.CustomErrorCodes;
import com.wzkris.common.security.oauth2.domain.model.LoginUser;
import com.wzkris.common.security.oauth2.enums.LoginType;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.user.api.RemoteSysUserApi;
import com.wzkris.user.api.domain.request.QueryPermsReq;
import com.wzkris.user.api.domain.response.SysPermissionResp;
import com.wzkris.user.api.domain.response.SysUserResp;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class LoginUserService extends UserInfoTemplate {

    private final RemoteSysUserApi remoteSysUserApi;

    @Override
    public LoginUser loadUserByPhoneNumber(String phoneNumber) {
        Result<SysUserResp> result = remoteSysUserApi.getByPhoneNumber(phoneNumber);
        if (!result.isSuccess()) {
            OAuth2ExceptionUtil.throwError(result.getCode(), CustomErrorCodes.VALIDATE_ERROR, result.getMessage());
        }
        return this.checkAndBuild(result.getData());
    }

    @Override
    public LoginUser loadByUsernameAndPassword(String username, String password) throws UsernameNotFoundException {
        Result<SysUserResp> result = remoteSysUserApi.getByUsername(username, password);
        if (!result.isSuccess()) {
            OAuth2ExceptionUtil.throwError(result.getCode(), CustomErrorCodes.VALIDATE_ERROR, result.getMessage());
        }
        return this.checkAndBuild(result.getData());
    }

    @Override
    public boolean checkLoginType(LoginType loginType) {
        return LoginType.SYSTEM_USER.equals(loginType);
    }

    /**
     * 构建登录用户
     */
    private LoginUser checkAndBuild(@Nullable SysUserResp userResp) {
        if (userResp == null) return null;
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
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        } else if (ObjUtil.equals(userResp.getTenantStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.tenant.disabled");
        } else if (userResp.getTenantExpired() != CommonConstants.NOT_EXPIRED_TIME
                && userResp.getTenantExpired() < System.currentTimeMillis()) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.tenant.expired");
        } else if (ObjUtil.equals(userResp.getPackageStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.package.disabled");
        }
    }

}

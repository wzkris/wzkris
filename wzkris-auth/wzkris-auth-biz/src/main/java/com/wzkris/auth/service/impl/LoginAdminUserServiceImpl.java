package com.wzkris.auth.service.impl;

import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.event.LoginEvent;
import com.wzkris.auth.remote.interfaces.admin.IAdminInfoRemote;
import com.wzkris.auth.remote.interfaces.admin.request.AdminPermsQueryRequest;
import com.wzkris.auth.remote.interfaces.admin.response.AdminInfoResponse;
import com.wzkris.auth.remote.interfaces.admin.response.AdminPermissionResponse;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.auth.service.LoginUserService;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.enums.IdentityTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.*;
import com.wzkris.common.security.exception.CustomErrorCodes;
import com.wzkris.common.security.model.AdminLoginUser;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LoginAdminUserServiceImpl implements LoginUserService {

    private final IAdminInfoRemote adminInfoRemote;

    private final PasswordEncoder passwordEncoder;

    @Nullable
    @Override
    public CommonAuthenticationToken loadUserByPhoneNumber(String phoneNumber) {
        Result<AdminInfoResponse> userResult = adminInfoRemote.queryByPhoneNumber(phoneNumber);

        if (!ResultUtil.check(userResult)) {
            return null;
        }
        AdminInfoResponse userResp = userResult.getData();

        try {
            return this.buildAuthenticationToken(userResp, LoginTypeEnum.SMS);
        } catch (Exception e) {
            this.recordFailedLog(userResp, LoginTypeEnum.SMS.getValue(), e.getMessage());
            throw e;
        }
    }

    @Nullable
    @Override
    public CommonAuthenticationToken loadByUsernameAndPassword(String username, String password) throws UsernameNotFoundException {
        Result<AdminInfoResponse> userResult = adminInfoRemote.queryByUsername(username);

        if (!ResultUtil.check(userResult)) {
            return null;
        }
        AdminInfoResponse userResp = userResult.getData();

        try {
            if (!passwordEncoder.matches(password, userResp.getPassword())) {
                OAuth2ExceptionUtil.throwErrorI18n(
                        BizBaseCodeEnum.REQUEST_ERROR.value(), CustomErrorCodes.VALIDATE_ERROR, "oauth2.passlogin.fail");
            }

            return this.buildAuthenticationToken(userResp, LoginTypeEnum.PASSWORD);
        } catch (Exception e) {
            this.recordFailedLog(userResp, LoginTypeEnum.PASSWORD.getValue(), e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean checkAuthType(AuthTypeEnum authType) {
        return AuthTypeEnum.ADMIN.equals(authType);
    }

    /**
     * 构建认证Token
     */
    private CommonAuthenticationToken buildAuthenticationToken(AdminInfoResponse userResp, LoginTypeEnum loginType) {
        // 校验用户状态
        this.checkAccount(userResp);

        // 获取权限信息
        Result<AdminPermissionResponse> permissionsResult = adminInfoRemote.queryPermission(
                new AdminPermsQueryRequest(userResp.getAdminId(), userResp.getDeptId()));
        if (!ResultUtil.check(permissionsResult)) {
            OAuth2ExceptionUtil.throwError(BizBaseCodeEnum.API_REQUEST_ERROR.value(), "query permission failed");
        }
        AdminPermissionResponse permissions = permissionsResult.getData();

        AdminLoginUser user = new AdminLoginUser();
        user.setUid(userResp.getAdminId());
        user.setAuthType(AuthTypeEnum.ADMIN);
        user.setIdentityType(SecurityConstants.SUPER_ADMIN_ID.equals(userResp.getAdminId())
                ? IdentityTypeEnum.SUPER
                : IdentityTypeEnum.NONE);
        user.setPhoneNumber(userResp.getPhoneNumber());
        user.setUsername(userResp.getUsername());
        user.setDeptScopes(permissions.getDeptScopes());

        Set<String> perms = permissions.getGrantedAuthority() != null
                ? new HashSet<>(permissions.getGrantedAuthority())
                : Collections.emptySet();

        return new CommonAuthenticationToken(user, perms, loginType);
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(AdminInfoResponse userResp) {
        if (StringUtil.equals(userResp.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.USER_DISABLED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }

    /**
     * 记录失败日志
     */
    private void recordFailedLog(AdminInfoResponse userResp, String loginType, String errorMsg) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        AdminLoginUser user = new AdminLoginUser();
        user.setUid(userResp.getAdminId());
        user.setAuthType(AuthTypeEnum.ADMIN);
        user.setIdentityType(IdentityTypeEnum.NONE);
        user.setUsername(userResp.getUsername());

        SpringUtil.getContext()
                .publishEvent(new LoginEvent(
                        user,
                        loginType,
                        false,
                        errorMsg,
                        ServletUtil.getClientIP(request),
                        getUserAgent(request),
                        TraceIdUtil.getOrGenerate()));
    }

}


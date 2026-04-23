package com.wzkris.auth.service.impl;

import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.event.LoginEvent;
import com.wzkris.auth.remote.interfaces.member.IMemberInfoRemote;
import com.wzkris.auth.remote.interfaces.member.request.MemberPermsQueryRequest;
import com.wzkris.auth.remote.interfaces.member.response.MemberInfoResponse;
import com.wzkris.auth.remote.interfaces.member.response.MemberPermissionResponse;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.auth.service.LoginUserService;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.enums.IdentityTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.*;
import com.wzkris.common.security.exception.CustomErrorCodes;
import com.wzkris.common.security.model.TenantLoginUser;
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

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LoginTenantUserServiceImpl implements LoginUserService {

    private final IMemberInfoRemote memberInfoRemote;

    private final PasswordEncoder passwordEncoder;

    @Nullable
    @Override
    public CommonAuthenticationToken loadUserByPhoneNumber(String phoneNumber) {
        Result<MemberInfoResponse> memberResult = memberInfoRemote.queryByPhoneNumber(phoneNumber);

        if (!ResultUtil.check(memberResult)) {
            return null;
        }
        MemberInfoResponse memberResp = memberResult.getData();

        try {
            return this.buildAuthenticationToken(memberResp, LoginTypeEnum.SMS);
        } catch (Exception e) {
            this.recordFailedLog(memberResp, LoginTypeEnum.SMS.getValue(), e.getMessage());
            throw e;
        }
    }

    @Nullable
    @Override
    public CommonAuthenticationToken loadByUsernameAndPassword(String username, String password) throws UsernameNotFoundException {
        Result<MemberInfoResponse> memberResult = memberInfoRemote.queryByUsername(username);

        if (!ResultUtil.check(memberResult)) {
            return null;
        }
        MemberInfoResponse memberResp = memberResult.getData();

        try {
            if (!passwordEncoder.matches(password, memberResp.getPassword())) {
                OAuth2ExceptionUtil.throwErrorI18n(
                        BizBaseCodeEnum.REQUEST_ERROR.value(), CustomErrorCodes.VALIDATE_ERROR, "oauth2.passlogin.fail");
            }

            return this.buildAuthenticationToken(memberResp, LoginTypeEnum.PASSWORD);
        } catch (Exception e) {
            this.recordFailedLog(memberResp, LoginTypeEnum.PASSWORD.getValue(), e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean checkAuthType(AuthTypeEnum authType) {
        return AuthTypeEnum.TENANT.equals(authType);
    }

    /**
     * 构建认证Token
     */
    private CommonAuthenticationToken buildAuthenticationToken(MemberInfoResponse memberResp, LoginTypeEnum loginType) {
        // 校验用户状态
        this.checkAccount(memberResp);

        // 获取权限信息
        Result<MemberPermissionResponse> permissionsResult = memberInfoRemote.queryPermission(
                new MemberPermsQueryRequest(memberResp.getMemberId(), memberResp.getTenantId()));
        if (!ResultUtil.check(permissionsResult)) {
            OAuth2ExceptionUtil.throwError(
                    BizBaseCodeEnum.API_REQUEST_ERROR.value(),
                    permissionsResult != null ? permissionsResult.getMessage() : "query permission failed");
        }
        MemberPermissionResponse permissions = permissionsResult.getData();

        TenantLoginUser loginUser = new TenantLoginUser();
        loginUser.setUid(memberResp.getMemberId());
        loginUser.setAuthType(AuthTypeEnum.TENANT);
        loginUser.setIdentityType(permissions.getAdmin()
                ? IdentityTypeEnum.SUPER
                : IdentityTypeEnum.NONE);
        loginUser.setUsername(memberResp.getUsername());
        loginUser.setTenantId(memberResp.getTenantId());

        Set<String> perms = permissions.getGrantedAuthority() != null
                ? new HashSet<>(permissions.getGrantedAuthority())
                : Collections.emptySet();

        return new CommonAuthenticationToken(loginUser, perms, loginType);
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(MemberInfoResponse memberResp) {
        if (StringUtil.equals(memberResp.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.USER_DISABLED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        } else if (StringUtil.equals(memberResp.getTenantStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.TENANT_DISABLED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.tenant.disabled");
        } else if (memberResp.getTenantExpired().isBefore(OffsetDateTime.now())) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.TENANT_EXPIRED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.tenant.expired");
        } else if (StringUtil.equals(memberResp.getPackageStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.TENANT_PACKAGE_EXPIRED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.package.disabled");
        }
    }

    /**
     * 构建租户登录用户视图
     */
    public TenantLoginUser buildLoginTenant(MemberInfoResponse memberResp) {
        // 校验用户状态
        this.checkAccount(memberResp);

        // 获取权限信息以判断身份类型
        Result<MemberPermissionResponse> permissionsResult = memberInfoRemote.queryPermission(
                new MemberPermsQueryRequest(memberResp.getMemberId(), memberResp.getTenantId()));
        if (!ResultUtil.check(permissionsResult)) {
            OAuth2ExceptionUtil.throwError(
                    BizBaseCodeEnum.API_REQUEST_ERROR.value(),
                    permissionsResult != null ? permissionsResult.getMessage() : "query permission failed");
        }
        MemberPermissionResponse permissions = permissionsResult.getData();

        TenantLoginUser loginUser = new TenantLoginUser();
        loginUser.setUid(memberResp.getMemberId());
        loginUser.setAuthType(AuthTypeEnum.TENANT);
        loginUser.setIdentityType(permissions.getAdmin()
                ? IdentityTypeEnum.SUPER
                : IdentityTypeEnum.NONE);
        loginUser.setUsername(memberResp.getUsername());
        loginUser.setTenantId(memberResp.getTenantId());

        return loginUser;
    }

    /**
     * 记录失败日志
     */
    private void recordFailedLog(MemberInfoResponse memberResp, String loginType, String errorMsg) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        TenantLoginUser loginUser = new TenantLoginUser();
        loginUser.setUid(memberResp.getMemberId());
        loginUser.setAuthType(AuthTypeEnum.TENANT);
        loginUser.setIdentityType(IdentityTypeEnum.NONE);
        loginUser.setUsername(memberResp.getUsername());
        loginUser.setTenantId(memberResp.getTenantId());

        SpringUtil.getContext()
                .publishEvent(new LoginEvent(
                        loginUser,
                        loginType,
                        false,
                        errorMsg,
                        ServletUtil.getClientIP(request),
                        getUserAgent(request),
                        TraceIdUtil.getOrGenerate()));
    }

}


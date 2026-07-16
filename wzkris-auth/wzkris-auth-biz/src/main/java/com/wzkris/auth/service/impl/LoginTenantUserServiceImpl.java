package com.wzkris.auth.service.impl;

import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.event.LoginEvent;
import com.wzkris.auth.remote.interfaces.member.IMemberInfoRemote;
import com.wzkris.auth.remote.interfaces.member.request.MemberPermsQueryRequest;
import com.wzkris.auth.remote.interfaces.member.request.MemberQueryRequest;
import com.wzkris.auth.remote.interfaces.member.response.MemberInfoResponse;
import com.wzkris.auth.remote.interfaces.member.response.MemberPermissionResponse;
import com.wzkris.auth.service.LoginUserService;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.*;
import com.wzkris.common.security.exception.CustomErrorCodes;
import com.wzkris.common.security.model.LoginTenantUser;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
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
    public UsernamePasswordAuthenticationToken loadUserByPhoneNumber(String phoneNumber) {
        MemberQueryRequest request = new MemberQueryRequest();
        request.setPhoneNumber(phoneNumber);
        Result<MemberInfoResponse> memberResult = memberInfoRemote.queryOne(request);

        if (!ResultUtil.check(memberResult)) {
            return null;
        }
        MemberInfoResponse memberResp = memberResult.getData();

        try {
            return this.buildAuthenticationToken(memberResp);
        } catch (Exception e) {
            this.recordFailedLog(memberResp, LoginTypeEnum.SMS.getValue(), e.getMessage());
            throw e;
        }
    }

    @Nullable
    @Override
    public UsernamePasswordAuthenticationToken loadByUsernameAndPassword(String username, String password) throws UsernameNotFoundException {
        MemberQueryRequest request = new MemberQueryRequest();
        request.setUsername(username);
        Result<MemberInfoResponse> memberResult = memberInfoRemote.queryOne(request);

        if (!ResultUtil.check(memberResult)) {
            return null;
        }
        MemberInfoResponse memberResp = memberResult.getData();

        try {
            if (!passwordEncoder.matches(password, memberResp.getPassword())) {
                OAuth2ExceptionUtil.throwErrorI18n(
                        BizBaseCodeEnum.REQUEST_ERROR.value(), CustomErrorCodes.VALIDATE_ERROR, "oauth2.passlogin.fail");
            }

            return this.buildAuthenticationToken(memberResp);
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
    private UsernamePasswordAuthenticationToken buildAuthenticationToken(MemberInfoResponse memberInfoResponse) {
        // 校验用户状态
        this.checkAccount(memberInfoResponse);

        // 获取权限信息
        Result<MemberPermissionResponse> permissionsResult = memberInfoRemote.queryPermission(
                new MemberPermsQueryRequest(memberInfoResponse.getMemberId(), memberInfoResponse.getTenantId()));
        if (!ResultUtil.check(permissionsResult)) {
            OAuth2ExceptionUtil.throwError(
                    BizBaseCodeEnum.API_REQUEST_ERROR.value(),
                    permissionsResult != null ? permissionsResult.getMessage() : "query permission failed");
        }
        MemberPermissionResponse permissions = permissionsResult.getData();

        LoginTenantUser tenantUser = new LoginTenantUser();
        tenantUser.setUid(memberInfoResponse.getMemberId());
        tenantUser.setAuthType(AuthTypeEnum.TENANT);
        tenantUser.setSuperUser(permissions.getAdmin());
        tenantUser.setUsername(memberInfoResponse.getUsername());
        tenantUser.setTenantId(memberInfoResponse.getTenantId());
        tenantUser.setRoles(permissions.getRoles());

        Set<String> perms = permissions.getGrantedAuthority() != null
                ? new HashSet<>(permissions.getGrantedAuthority())
                : Collections.emptySet();

        return UsernamePasswordAuthenticationToken.authenticated(
                tenantUser, null, AuthorityUtils.createAuthorityList(perms));
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(MemberInfoResponse memberResp) {
        if (StringUtil.equals(memberResp.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.USER_DISABLED.getCode(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        } else if (StringUtil.equals(memberResp.getTenantStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.TENANT_DISABLED.getCode(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.tenant.disabled");
        } else if (memberResp.getTenantExpired().isBefore(OffsetDateTime.now())) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.TENANT_EXPIRED.getCode(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.tenant.expired");
        } else if (StringUtil.equals(memberResp.getPackageStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.TENANT_PACKAGE_EXPIRED.getCode(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.package.disabled");
        }
    }

    /**
     * 记录失败日志
     */
    private void recordFailedLog(MemberInfoResponse memberResp, String loginType, String errorMsg) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        LoginTenantUser tenantUser = new LoginTenantUser();
        tenantUser.setUid(memberResp.getMemberId());
        tenantUser.setAuthType(AuthTypeEnum.TENANT);
        tenantUser.setSuperUser(false);
        tenantUser.setUsername(memberResp.getUsername());
        tenantUser.setTenantId(memberResp.getTenantId());

        SpringUtil.getContext()
                .publishEvent(new LoginEvent(
                        tenantUser,
                        loginType,
                        false,
                        errorMsg,
                        ServletUtil.getClientIP(request),
                        getUserAgent(request),
                        TraceIdUtil.getOrGenerate()));
    }

}


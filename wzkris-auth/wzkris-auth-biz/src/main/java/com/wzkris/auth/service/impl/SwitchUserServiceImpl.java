package com.wzkris.auth.service.impl;

import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.event.LoginEvent;
import com.wzkris.auth.remote.interfaces.member.IMemberInfoRemote;
import com.wzkris.auth.remote.interfaces.member.request.MemberPermsQueryRequest;
import com.wzkris.auth.remote.interfaces.member.request.TenantIdRequest;
import com.wzkris.auth.remote.interfaces.member.response.MemberInfoResponse;
import com.wzkris.auth.remote.interfaces.member.response.MemberPermissionResponse;
import com.wzkris.auth.service.SwitchUserService;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.enums.IdentityTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.*;
import com.wzkris.common.security.model.LoginAdminUser;
import com.wzkris.common.security.model.LoginTenantUser;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import com.wzkris.common.web.utils.UserAgentUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nl.basjes.parse.useragent.UserAgent;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
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
public class SwitchUserServiceImpl implements SwitchUserService {

    private final IMemberInfoRemote memberInfoRemote;

    private final TokenService tokenService;

    @Nullable
    @Override
    public UsernamePasswordAuthenticationToken switchToTenant(LoginAdminUser adminUser, Long tenantId) {
        Result<MemberInfoResponse> memberResult = memberInfoRemote.queryAdministratorByTenantId(
                new TenantIdRequest(tenantId));
        if (!ResultUtil.check(memberResult)) {
            return null;
        }
        MemberInfoResponse memberResp = memberResult.getData();

        try {
            UsernamePasswordAuthenticationToken token = buildTenantAuthenticationToken(memberResp);
            LoginTenantUser tenantUser = (LoginTenantUser) token.getPrincipal();
            tenantUser.setActorUid(adminUser.getUid());
            tenantUser.setActorAuthType(AuthTypeEnum.ADMIN);
            return token;
        } catch (Exception e) {
            recordTenantFailedLog(memberResp, e.getMessage());
            throw e;
        }
    }

    @Nullable
    @Override
    public UsernamePasswordAuthenticationToken switchToAdmin(Long actorUid) {
        String adminType = AuthTypeEnum.ADMIN.getValue();
        var loadedUser = tokenService.loadLoginUserByUid(adminType, actorUid);
        if (!(loadedUser instanceof LoginAdminUser adminUser)) {
            return null;
        }
        Set<String> perms = tokenService.loadPermissionsByUid(adminType, actorUid);
        if (perms == null) {
            perms = Collections.emptySet();
        }
        return UsernamePasswordAuthenticationToken.authenticated(
                adminUser, null, AuthorityUtils.createAuthorityList(perms));
    }

    private UsernamePasswordAuthenticationToken buildTenantAuthenticationToken(MemberInfoResponse memberInfoResponse) {
        checkTenantAccount(memberInfoResponse);

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
        tenantUser.setIdentityType(permissions.getAdmin()
                ? IdentityTypeEnum.SUPER
                : IdentityTypeEnum.NONE);
        tenantUser.setUsername(memberInfoResponse.getUsername());
        tenantUser.setTenantId(memberInfoResponse.getTenantId());

        Set<String> perms = permissions.getGrantedAuthority() != null
                ? new HashSet<>(permissions.getGrantedAuthority())
                : Collections.emptySet();

        return UsernamePasswordAuthenticationToken.authenticated(
                tenantUser, null, AuthorityUtils.createAuthorityList(perms));
    }

    private void checkTenantAccount(MemberInfoResponse memberResp) {
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

    private void recordTenantFailedLog(MemberInfoResponse memberResp, String errorMsg) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        LoginTenantUser tenantUser = new LoginTenantUser();
        tenantUser.setUid(memberResp.getMemberId());
        tenantUser.setAuthType(AuthTypeEnum.TENANT);
        tenantUser.setIdentityType(IdentityTypeEnum.NONE);
        tenantUser.setUsername(memberResp.getUsername());
        tenantUser.setTenantId(memberResp.getTenantId());

        UserAgent.ImmutableUserAgent userAgent = UserAgentUtil.INSTANCE.parse(request.getHeader(HttpHeaders.USER_AGENT));
        SpringUtil.getContext()
                .publishEvent(new LoginEvent(
                        tenantUser,
                        LoginTypeEnum.SWITCH.getValue(),
                        false,
                        errorMsg,
                        ServletUtil.getClientIP(request),
                        userAgent.getUserAgentString(),
                        TraceIdUtil.getOrGenerate()));
    }

}

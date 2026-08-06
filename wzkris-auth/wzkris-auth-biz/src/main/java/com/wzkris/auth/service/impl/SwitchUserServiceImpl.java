package com.wzkris.auth.service.impl;

import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.remote.interfaces.member.IMemberRemote;
import com.wzkris.auth.remote.interfaces.member.request.MemberPermsQueryRequest;
import com.wzkris.auth.remote.interfaces.member.request.TenantIdRequest;
import com.wzkris.auth.remote.interfaces.member.response.MemberQueryResponse;
import com.wzkris.auth.service.SwitchUserService;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.model.*;
import com.wzkris.common.core.support.LoginUser;
import com.wzkris.common.core.utils.ResultUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.authentication.RoleContextAuthenticationToken;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SwitchUserServiceImpl implements SwitchUserService {

    private final IMemberRemote memberRemote;

    private final TokenService tokenService;

    @Nullable
    @Override
    public UsernamePasswordAuthenticationToken switchToTenant(LoginUser loginUser, Long tenantId, String actorSid) {
        Result<MemberQueryResponse> memberResult = memberRemote.queryAdministratorByTenantId(
                new TenantIdRequest(tenantId));
        if (!ResultUtil.check(memberResult)) {
            return null;
        }
        MemberQueryResponse memberResp = memberResult.getData();

        UsernamePasswordAuthenticationToken token = buildTenantAuthenticationToken(memberResp);
        DefaultLoginUser switchedUser = (DefaultLoginUser) token.getPrincipal();
        switchedUser.setActor(ActorInfo.of(loginUser.getUid(), AuthTypeEnum.ADMIN, actorSid));
        return token;
    }

    @Nullable
    @Override
    public UsernamePasswordAuthenticationToken switchBack(Long actorUid, AuthTypeEnum authTypeEnum) {
        var loadedUser = tokenService.loadLoginUserByUid(authTypeEnum.getValue(), actorUid);
        if (!(loadedUser instanceof LoginUser loginUser)) {
            return null;
        }

        RoleContext roleContext = tokenService.loadRoleContextByUid(authTypeEnum.getValue(), actorUid);

        return RoleContextAuthenticationToken.authenticated(loginUser, null, roleContext);
    }

    private UsernamePasswordAuthenticationToken buildTenantAuthenticationToken(MemberQueryResponse userResp) {
        checkTenantAccount(userResp);

        Result<List<UserRole>> userRoleR = memberRemote.queryPermission(
                new MemberPermsQueryRequest(userResp.getId(), userResp.getTenantId()));
        if (!ResultUtil.check(userRoleR)) {
            OAuth2ExceptionUtil.throwError(
                    BizBaseCodeEnum.API_REQUEST_ERROR.value(), userRoleR.getMessage());
        }

        DefaultLoginUser loginUser = new DefaultLoginUser();
        loginUser.setUid(userResp.getId());
        loginUser.setAuthType(AuthTypeEnum.TENANT);
        loginUser.setName(userResp.getUsername());
        loginUser.setTenantId(userResp.getTenantId());

        // 租户管理员通过角色名判断
        boolean isSuperUser = userRoleR.getData().stream()
                .anyMatch(r -> SecurityConstants.SUPER_ADMIN_NAME.equals(r.getName()));

        RoleContext roleContext = new RoleContext(userRoleR.getData(), isSuperUser);

        return RoleContextAuthenticationToken.authenticated(loginUser, null, roleContext);
    }

    private void checkTenantAccount(MemberQueryResponse memberResp) {
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

}

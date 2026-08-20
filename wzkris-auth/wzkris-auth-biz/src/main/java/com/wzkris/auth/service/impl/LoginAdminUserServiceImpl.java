package com.wzkris.auth.service.impl;

import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.event.LoginEvent;
import com.wzkris.auth.remote.interfaces.admin.IAdminRemote;
import com.wzkris.auth.remote.interfaces.admin.request.AdminPermissionQueryRequest;
import com.wzkris.auth.remote.interfaces.admin.request.AdminQueryRequest;
import com.wzkris.auth.remote.interfaces.admin.response.AdminListResponse;
import com.wzkris.auth.service.LoginUserService;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.model.DefaultLoginUser;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.RoleContext;
import com.wzkris.common.core.model.UserRole;
import com.wzkris.common.core.utils.*;
import com.wzkris.common.security.authentication.RoleContextAuthenticationToken;
import com.wzkris.common.security.exception.CustomErrorCodes;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginAdminUserServiceImpl implements LoginUserService {

    private final IAdminRemote adminRemote;

    private final PasswordEncoder passwordEncoder;

    @Nullable
    @Override
    public UsernamePasswordAuthenticationToken loadUserByPhoneNumber(String phoneNumber, @Nullable String wxCode, @Nullable String appid) {
        AdminQueryRequest request = new AdminQueryRequest();
        request.setPhoneNumber(phoneNumber);
        Result<List<AdminListResponse>> userResult = adminRemote.queryList(request);

        if (!ResultUtil.check(userResult)) {
            return null;
        }

        if (CollectionUtils.isEmpty(userResult.getData()) || userResult.getData().size() > 1) {
            return null;
        }

        AdminListResponse userResp = userResult.getData().getFirst();

        try {
            return this.buildAuthenticationToken(userResp);
        } catch (Exception e) {
            this.recordFailedLog(userResp, LoginTypeEnum.SMS.getValue(), e.getMessage());
            throw e;
        }
    }

    @Nullable
    @Override
    public UsernamePasswordAuthenticationToken loadByUsernameAndPassword(String username, String password) throws UsernameNotFoundException {
        AdminQueryRequest request = new AdminQueryRequest();
        request.setUsername(username);
        Result<List<AdminListResponse>> userResult = adminRemote.queryList(request);

        if (!ResultUtil.check(userResult)) {
            return null;
        }

        if (CollectionUtils.isEmpty(userResult.getData()) || userResult.getData().size() > 1) {
            return null;
        }

        AdminListResponse userResp = userResult.getData().getFirst();

        try {
            if (!passwordEncoder.matches(password, userResp.getPassword())) {
                OAuth2ExceptionUtil.throwErrorI18n(
                        BizBaseCodeEnum.REQUEST_ERROR.value(), CustomErrorCodes.VALIDATE_ERROR, "oauth2.passlogin.fail");
            }

            return this.buildAuthenticationToken(userResp);
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
    private UsernamePasswordAuthenticationToken buildAuthenticationToken(AdminListResponse userResp) {
        // 校验用户状态
        this.checkAccount(userResp);

        // 获取权限信息
        Result<List<UserRole>> userRoleR = adminRemote.queryPermission(
                new AdminPermissionQueryRequest(userResp.getId(), userResp.getDeptId()));
        if (!ResultUtil.check(userRoleR)) {
            OAuth2ExceptionUtil.throwError(BizBaseCodeEnum.API_REQUEST_ERROR.value(), "query permission failed");
        }

        DefaultLoginUser loginUser = new DefaultLoginUser();
        loginUser.setUid(userResp.getId());
        loginUser.setAuthType(AuthTypeEnum.ADMIN);
        loginUser.setName(userResp.getUsername());

        RoleContext roleContext = new RoleContext(userRoleR.getData(),
                SecurityConstants.SUPER_ADMIN_ID.equals(userResp.getId()));

        return RoleContextAuthenticationToken.authenticated(loginUser, null, roleContext);
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(AdminListResponse userResp) {
        if (StringUtil.equals(userResp.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.USER_DISABLED.getCode(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }

    /**
     * 记录失败日志
     */
    private void recordFailedLog(AdminListResponse userResp, String loginType, String errorMsg) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        DefaultLoginUser loginUser = new DefaultLoginUser();
        loginUser.setUid(userResp.getId());
        loginUser.setAuthType(AuthTypeEnum.ADMIN);
        loginUser.setName(userResp.getUsername());

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

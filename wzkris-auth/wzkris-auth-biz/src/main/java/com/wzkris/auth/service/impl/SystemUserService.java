package com.wzkris.auth.service.impl;

import com.wzkris.auth.listener.event.LoginTokenEvent;
import com.wzkris.auth.rmi.domain.SystemUser;
import com.wzkris.auth.rmi.enums.AuthenticatedType;
import com.wzkris.auth.security.constants.OAuth2LoginTypeConstant;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.captcha.service.CaptchaService;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.core.utils.UserAgentUtil;
import com.wzkris.common.security.exception.CustomErrorCodes;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.user.rmi.RmiSysUserFeign;
import com.wzkris.user.rmi.domain.req.QueryPermsReq;
import com.wzkris.user.rmi.domain.resp.SysPermissionResp;
import com.wzkris.user.rmi.domain.resp.SysUserResp;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class SystemUserService extends UserInfoTemplate {

    private final CaptchaService captchaService;

    private final RmiSysUserFeign rmiSysUserFeign;

    private final PasswordEncoder passwordEncoder;

    @Nullable
    @Override
    public CorePrincipal loadUserByPhoneNumber(String phoneNumber) {
        SysUserResp userResp = rmiSysUserFeign.getByPhoneNumber(phoneNumber);

        if (userResp == null) {
            captchaService.lockAccount(phoneNumber, 600);
            return null;
        }

        try {
            return this.buildLoginUser(userResp);
        } catch (Exception e) {
            this.recordFailedLog(userResp, OAuth2LoginTypeConstant.SMS, e.getMessage());
            throw e;
        }
    }

    @Nullable
    @Override
    public CorePrincipal loadByUsernameAndPassword(String username, String password) throws UsernameNotFoundException {
        SysUserResp userResp = rmiSysUserFeign.getByUsername(username);

        if (userResp == null) {
            captchaService.lockAccount(username, 600);
            return null;
        }

        try {
            if (!passwordEncoder.matches(password, userResp.getPassword())) {
                OAuth2ExceptionUtil.throwErrorI18n(
                        BizCode.PRECONDITION_FAILED.value(), CustomErrorCodes.VALIDATE_ERROR, "oauth2.passlogin.fail");
            }

            return this.buildLoginUser(userResp);
        } catch (Exception e) {
            this.recordFailedLog(userResp, OAuth2LoginTypeConstant.PASSWORD, e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean checkAuthenticatedType(AuthenticatedType authenticatedType) {
        return AuthenticatedType.SYSTEM_USER.equals(authenticatedType);
    }

    /**
     * 构建登录用户
     */
    private SystemUser buildLoginUser(SysUserResp userResp) {
        // 校验用户状态
        this.checkAccount(userResp);

        // 获取权限信息
        SysPermissionResp permissions = rmiSysUserFeign.getPermission(
                new QueryPermsReq(userResp.getUserId(), userResp.getTenantId(), userResp.getDeptId()));

        SystemUser systemUser = new SystemUser(userResp.getUserId(), new HashSet<>(permissions.getGrantedAuthority()));
        systemUser.setAdmin(permissions.getAdmin());
        systemUser.setUsername(userResp.getUsername());
        systemUser.setTenantId(userResp.getTenantId());
        systemUser.setDeptScopes(permissions.getDeptScopes());

        return systemUser;
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(SysUserResp userResp) {
        if (StringUtil.equals(userResp.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        } else if (StringUtil.equals(userResp.getTenantStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.tenant.disabled");
        } else if (userResp.getTenantExpired().getTime() < System.currentTimeMillis()) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.tenant.expired");
        } else if (StringUtil.equals(userResp.getPackageStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.package.disabled");
        }
    }

    /**
     * 记录失败日志
     */
    private void recordFailedLog(SysUserResp userResp, String grantType, String errorMsg) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SystemUser user = new SystemUser(userResp.getUserId(), null);
        user.setUsername(userResp.getUsername());
        user.setTenantId(userResp.getTenantId());
        SpringUtil.getContext()
                .publishEvent(new LoginTokenEvent(
                        user,
                        null,
                        grantType,
                        CommonConstants.STATUS_DISABLE,
                        errorMsg,
                        ServletUtil.getClientIP(request),
                        UserAgentUtil.INSTANCE.parse(request.getHeader(HttpHeaders.USER_AGENT))));
    }

}

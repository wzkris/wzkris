package com.wzkris.auth.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.auth.oauth2.constants.OAuth2GrantTypeConstant;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.captcha.service.CaptchaService;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.security.oauth2.constants.CustomErrorCodes;
import com.wzkris.common.security.oauth2.domain.model.LoginUser;
import com.wzkris.common.security.oauth2.enums.LoginType;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.user.rmi.RmiSysUserService;
import com.wzkris.user.rmi.domain.req.QueryPermsReq;
import com.wzkris.user.rmi.domain.resp.SysPermissionResp;
import com.wzkris.user.rmi.domain.resp.SysUserResp;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class LoginUserService extends UserInfoTemplate {

    private final CaptchaService captchaService;

    private final RmiSysUserService rmiSysUserService;

    private final PasswordEncoder passwordEncoder;

    @Nullable
    @Override
    public LoginUser loadUserByPhoneNumber(String phoneNumber) {
        SysUserResp userResp = rmiSysUserService.getByPhoneNumber(phoneNumber);

        if (userResp == null) {
            captchaService.lockAccount(phoneNumber, 600);
            return null;
        }

        try {
            return this.buildLoginUser(userResp);
        } catch (Exception e) {
            this.recordFailedLog(userResp, OAuth2GrantTypeConstant.SMS, e.getMessage());
            throw e;
        }
    }

    @Nullable
    @Override
    public LoginUser loadByUsernameAndPassword(String username, String password) throws UsernameNotFoundException {
        SysUserResp userResp = rmiSysUserService.getByUsername(username);

        if (userResp == null) {
            captchaService.lockAccount(username, 600);
            return null;
        }

        try {
            if (!passwordEncoder.matches(password, userResp.getPassword())) {
                OAuth2ExceptionUtil.throwErrorI18n(BizCode.PRECONDITION_FAILED.value(), CustomErrorCodes.VALIDATE_ERROR, "oauth2.passlogin.fail");
            }

            return this.buildLoginUser(userResp);
        } catch (Exception e) {
            this.recordFailedLog(userResp, AuthorizationGrantType.PASSWORD.getValue(), e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean checkLoginType(LoginType loginType) {
        return LoginType.SYSTEM_USER.equals(loginType);
    }

    /**
     * 构建登录用户
     */
    private LoginUser buildLoginUser(SysUserResp userResp) {
        // 校验用户状态
        this.checkAccount(userResp);

        // 获取权限信息
        SysPermissionResp permissions = rmiSysUserService
                .getPermission(new QueryPermsReq(userResp.getUserId(), userResp.getTenantId(), userResp.getDeptId()));

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
        } else if (userResp.getTenantExpired().getTime() < System.currentTimeMillis()) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.tenant.expired");
        } else if (ObjUtil.equals(userResp.getPackageStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.package.disabled");
        }
    }

    /**
     * 记录失败日志
     */
    private void recordFailedLog(SysUserResp userResp, String grantType, String errorMsg) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(userResp.getUserId());
        loginUser.setUsername(userResp.getUsername());
        loginUser.setTenantId(userResp.getTenantId());
        SpringUtil.getContext().publishEvent(
                new LoginEvent("", loginUser, grantType,
                        CommonConstants.STATUS_DISABLE, errorMsg,
                        ServletUtil.getClientIP(request),
                        UserAgentUtil.parse(request.getHeader(HttpHeaders.USER_AGENT))
                )
        );
    }
}

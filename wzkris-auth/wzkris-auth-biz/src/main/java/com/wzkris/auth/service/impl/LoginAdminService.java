package com.wzkris.auth.service.impl;

import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.enums.IdentityTypeEnum;
import com.wzkris.common.core.model.LoginUser;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.exception.CustomErrorCodes;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import com.wzkris.common.web.utils.UserAgentUtil;
import com.wzkris.usercenter.httpservice.admin.AdminInfoHttpService;
import com.wzkris.usercenter.httpservice.admin.req.QueryAdminPermsReq;
import com.wzkris.usercenter.httpservice.admin.resp.AdminInfoResp;
import com.wzkris.usercenter.httpservice.admin.resp.AdminPermissionResp;
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LoginAdminService extends UserInfoTemplate {

    private final CaptchaService captchaService;

    private final AdminInfoHttpService adminInfoHttpService;

    private final PasswordEncoder passwordEncoder;

    @Nullable
    @Override
    public CommonAuthenticationToken loadUserByPhoneNumber(String phoneNumber) {
        AdminInfoResp userResp = adminInfoHttpService.getByPhoneNumber(phoneNumber);

        if (userResp == null) {
            captchaService.freezeAccount(phoneNumber, 60);
            return null;
        }

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
        AdminInfoResp userResp = adminInfoHttpService.getByUsername(username);

        if (userResp == null) {
            captchaService.freezeAccount(username, 60);
            return null;
        }

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
    private CommonAuthenticationToken buildAuthenticationToken(AdminInfoResp userResp, LoginTypeEnum loginType) {
        // 校验用户状态
        this.checkAccount(userResp);

        // 获取权限信息
        AdminPermissionResp permissions = adminInfoHttpService.getPermission(
                new QueryAdminPermsReq(userResp.getAdminId(), userResp.getDeptId()));

        LoginUser loginUser = new LoginUser();
        loginUser.setUid(userResp.getAdminId());
        loginUser.setAuthType(AuthTypeEnum.ADMIN);
        loginUser.setIdentityType(SecurityConstants.SUPER_ADMIN_ID.equals(userResp.getAdminId())
                ? IdentityTypeEnum.ADMIN_SUPER
                : IdentityTypeEnum.ADMIN_NORMAL);
        loginUser.setPhoneNumber(userResp.getPhoneNumber());
        loginUser.setUsername(userResp.getUsername());

        Set<String> perms = permissions.getGrantedAuthority() != null
                ? new HashSet<>(permissions.getGrantedAuthority())
                : Collections.emptySet();

        return new CommonAuthenticationToken(loginUser, perms, loginType);
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(AdminInfoResp userResp) {
        if (StringUtil.equals(userResp.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.USER_DISABLED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }

    /**
     * 记录失败日志
     */
    private void recordFailedLog(AdminInfoResp userResp, String loginType, String errorMsg) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        LoginUser loginUser = new LoginUser();
        loginUser.setUid(userResp.getAdminId());
        loginUser.setAuthType(AuthTypeEnum.ADMIN);
        loginUser.setIdentityType(IdentityTypeEnum.ADMIN_NORMAL);
        loginUser.setUsername(userResp.getUsername());

        SpringUtil.getContext()
                .publishEvent(new LoginEvent(
                        loginUser,
                        loginType,
                        false,
                        errorMsg,
                        ServletUtil.getClientIP(request),
                        UserAgentUtil.INSTANCE.parse(request.getHeader(HttpHeaders.USER_AGENT))));
    }

}

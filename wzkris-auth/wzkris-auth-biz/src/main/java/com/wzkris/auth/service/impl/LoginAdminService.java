package com.wzkris.auth.service.impl;

import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.core.model.domain.LoginAdmin;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.exception.CustomErrorCodes;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.common.web.utils.UserAgentUtil;
import com.wzkris.principal.feign.admin.AdminInfoFeign;
import com.wzkris.principal.feign.admin.req.QueryAdminPermsReq;
import com.wzkris.principal.feign.admin.resp.AdminPermissionResp;
import com.wzkris.principal.feign.admin.resp.adminInfoResp;
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

@Service
@RequiredArgsConstructor
public class LoginAdminService extends UserInfoTemplate {

    private final CaptchaService captchaService;

    private final AdminInfoFeign adminInfoFeign;

    private final PasswordEncoder passwordEncoder;

    @Nullable
    @Override
    public MyPrincipal loadUserByPhoneNumber(String phoneNumber) {
        adminInfoResp userResp = adminInfoFeign.getByPhoneNumber(phoneNumber);

        if (userResp == null) {
            captchaService.freezeAccount(phoneNumber, 60);
            return null;
        }

        try {
            return this.buildLoginAdmin(userResp);
        } catch (Exception e) {
            this.recordFailedLog(userResp, LoginTypeEnum.SMS.getValue(), e.getMessage());
            throw e;
        }
    }

    @Nullable
    @Override
    public MyPrincipal loadByUsernameAndPassword(String username, String password) throws UsernameNotFoundException {
        adminInfoResp userResp = adminInfoFeign.getByUsername(username);

        if (userResp == null) {
            captchaService.freezeAccount(username, 60);
            return null;
        }

        try {
            if (!passwordEncoder.matches(password, userResp.getPassword())) {
                OAuth2ExceptionUtil.throwErrorI18n(
                        BizBaseCodeEnum.REQUEST_ERROR.value(), CustomErrorCodes.VALIDATE_ERROR, "oauth2.passlogin.fail");
            }

            return this.buildLoginAdmin(userResp);
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
     * 构建登录用户
     */
    private LoginAdmin buildLoginAdmin(adminInfoResp userResp) {
        // 校验用户状态
        this.checkAccount(userResp);

        // 获取权限信息
        AdminPermissionResp permissions = adminInfoFeign.getPermission(
                new QueryAdminPermsReq(userResp.getAdminId(), userResp.getDeptId()));

        LoginAdmin loginAdmin = new LoginAdmin(userResp.getAdminId(), new HashSet<>(permissions.getGrantedAuthority()));
        loginAdmin.setAdmin(permissions.getAdmin());
        loginAdmin.setUsername(userResp.getUsername());
        loginAdmin.setDeptScopes(permissions.getDeptScopes());

        return loginAdmin;
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(adminInfoResp userResp) {
        if (StringUtil.equals(userResp.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.USER_DISABLED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }

    /**
     * 记录失败日志
     */
    private void recordFailedLog(adminInfoResp userResp, String loginType, String errorMsg) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        LoginAdmin loginAdmin = new LoginAdmin(userResp.getAdminId(), Collections.emptySet());
        loginAdmin.setAdmin(false);
        loginAdmin.setUsername(userResp.getUsername());

        SpringUtil.getContext()
                .publishEvent(new LoginEvent(
                        loginAdmin,
                        null,
                        loginType,
                        false,
                        errorMsg,
                        ServletUtil.getClientIP(request),
                        UserAgentUtil.INSTANCE.parse(request.getHeader(HttpHeaders.USER_AGENT))));
    }

}

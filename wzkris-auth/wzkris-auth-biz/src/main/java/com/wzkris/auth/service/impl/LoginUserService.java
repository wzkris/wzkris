package com.wzkris.auth.service.impl;

import com.wzkris.auth.enums.BizLoginCode;
import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.auth.security.constants.OAuth2LoginTypeConstant;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.enums.AuthType;
import com.wzkris.common.security.exception.CustomErrorCodes;
import com.wzkris.common.security.model.domain.LoginUser;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.common.web.utils.UserAgentUtil;
import com.wzkris.user.feign.userinfo.UserInfoFeign;
import com.wzkris.user.feign.userinfo.req.QueryPermsReq;
import com.wzkris.user.feign.userinfo.resp.PermissionResp;
import com.wzkris.user.feign.userinfo.resp.UserInfoResp;
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
public class LoginUserService extends UserInfoTemplate {

    private final CaptchaService captchaService;

    private final UserInfoFeign userInfoFeign;

    private final PasswordEncoder passwordEncoder;

    @Nullable
    @Override
    public CorePrincipal loadUserByPhoneNumber(String phoneNumber) {
        UserInfoResp userResp = userInfoFeign.getByPhoneNumber(phoneNumber);

        if (userResp == null) {
            captchaService.freezeAccount(phoneNumber, 60);
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
        UserInfoResp userResp = userInfoFeign.getByUsername(username);

        if (userResp == null) {
            captchaService.freezeAccount(username, 60);
            return null;
        }

        try {
            if (!passwordEncoder.matches(password, userResp.getPassword())) {
                OAuth2ExceptionUtil.throwErrorI18n(
                        BizBaseCode.BAD_REQUEST.value(), CustomErrorCodes.VALIDATE_ERROR, "oauth2.passlogin.fail");
            }

            return this.buildLoginUser(userResp);
        } catch (Exception e) {
            this.recordFailedLog(userResp, OAuth2LoginTypeConstant.PASSWORD, e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean checkAuthType(AuthType authType) {
        return AuthType.USER.equals(authType);
    }

    /**
     * 构建登录用户
     */
    private LoginUser buildLoginUser(UserInfoResp userResp) {
        // 校验用户状态
        this.checkAccount(userResp);

        // 获取权限信息
        PermissionResp permissions = userInfoFeign.getPermission(
                new QueryPermsReq(userResp.getUserId(), userResp.getTenantId(), userResp.getDeptId()));

        LoginUser loginUser = new LoginUser(userResp.getUserId(), new HashSet<>(permissions.getGrantedAuthority()));
        loginUser.setAdmin(permissions.getAdmin());
        loginUser.setUsername(userResp.getUsername());
        loginUser.setTenantId(userResp.getTenantId());
        loginUser.setDeptScopes(permissions.getDeptScopes());

        return loginUser;
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(UserInfoResp userResp) {
        if (StringUtil.equals(userResp.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCode.USER_DISABLED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        } else if (StringUtil.equals(userResp.getTenantStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCode.TENANT_DISABLED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.tenant.disabled");
        } else if (userResp.getTenantExpired().getTime() < System.currentTimeMillis()) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCode.TENANT_EXPIRED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.tenant.expired");
        } else if (StringUtil.equals(userResp.getPackageStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCode.TENANT_PACKAGE_EXPIRED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.package.disabled");
        }
    }

    /**
     * 记录失败日志
     */
    private void recordFailedLog(UserInfoResp userResp, String loginType, String errorMsg) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        LoginUser user = new LoginUser(userResp.getUserId(), null);
        user.setUsername(userResp.getUsername());
        user.setTenantId(userResp.getTenantId());
        SpringUtil.getContext()
                .publishEvent(new LoginEvent(
                        user,
                        null,
                        loginType,
                        CommonConstants.FAIL,
                        errorMsg,
                        ServletUtil.getClientIP(request),
                        UserAgentUtil.INSTANCE.parse(request.getHeader(HttpHeaders.USER_AGENT))));
    }

}

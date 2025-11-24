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
import com.wzkris.common.core.model.domain.LoginTenant;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.exception.CustomErrorCodes;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.common.web.utils.UserAgentUtil;
import com.wzkris.principal.httpservice.member.MemberInfoHttpService;
import com.wzkris.principal.httpservice.member.req.QueryMemberPermsReq;
import com.wzkris.principal.httpservice.member.resp.MemberInfoResp;
import com.wzkris.principal.httpservice.member.resp.MemberPermissionResp;
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
public class LoginTenantService extends UserInfoTemplate {

    private final CaptchaService captchaService;

    private final MemberInfoHttpService memberInfoHttpService;

    private final PasswordEncoder passwordEncoder;

    @Nullable
    @Override
    public MyPrincipal loadUserByPhoneNumber(String phoneNumber) {
        MemberInfoResp memberResp = memberInfoHttpService.getByPhoneNumber(phoneNumber);

        if (memberResp == null) {
            captchaService.freezeAccount(phoneNumber, 60);
            return null;
        }

        try {
            return this.buildLoginTenant(memberResp);
        } catch (Exception e) {
            this.recordFailedLog(memberResp, LoginTypeEnum.SMS.getValue(), e.getMessage());
            throw e;
        }
    }

    @Nullable
    @Override
    public MyPrincipal loadByUsernameAndPassword(String username, String password) throws UsernameNotFoundException {
        MemberInfoResp memberResp = memberInfoHttpService.getByUsername(username);

        if (memberResp == null) {
            captchaService.freezeAccount(username, 60);
            return null;
        }

        try {
            if (!passwordEncoder.matches(password, memberResp.getPassword())) {
                OAuth2ExceptionUtil.throwErrorI18n(
                        BizBaseCodeEnum.REQUEST_ERROR.value(), CustomErrorCodes.VALIDATE_ERROR, "oauth2.passlogin.fail");
            }

            return this.buildLoginTenant(memberResp);
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
     * 构建登录用户
     */
    public LoginTenant buildLoginTenant(MemberInfoResp memberResp) {
        // 校验用户状态
        this.checkAccount(memberResp);

        // 获取权限信息
        MemberPermissionResp permissions = memberInfoHttpService.getPermission(
                new QueryMemberPermsReq(memberResp.getMemberId(), memberResp.getTenantId()));

        LoginTenant loginTenant = new LoginTenant(memberResp.getMemberId(), new HashSet<>(permissions.getGrantedAuthority()));
        loginTenant.setAdmin(permissions.getAdmin());
        loginTenant.setUsername(memberResp.getUsername());
        loginTenant.setTenantId(memberResp.getTenantId());
        return loginTenant;
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(MemberInfoResp memberResp) {
        if (StringUtil.equals(memberResp.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.USER_DISABLED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        } else if (StringUtil.equals(memberResp.getTenantStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.TENANT_DISABLED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.tenant.disabled");
        } else if (memberResp.getTenantExpired().getTime() < System.currentTimeMillis()) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.TENANT_EXPIRED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.tenant.expired");
        } else if (StringUtil.equals(memberResp.getPackageStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.TENANT_PACKAGE_EXPIRED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.package.disabled");
        }
    }

    /**
     * 记录失败日志
     */
    private void recordFailedLog(MemberInfoResp memberResp, String loginType, String errorMsg) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        LoginTenant loginTenant = new LoginTenant(memberResp.getMemberId(), Collections.emptySet());
        loginTenant.setAdmin(false);
        loginTenant.setUsername(memberResp.getUsername());
        loginTenant.setTenantId(memberResp.getTenantId());

        SpringUtil.getContext()
                .publishEvent(new LoginEvent(
                        loginTenant,
                        null,
                        loginType,
                        false,
                        errorMsg,
                        ServletUtil.getClientIP(request),
                        UserAgentUtil.INSTANCE.parse(request.getHeader(HttpHeaders.USER_AGENT))));
    }

}

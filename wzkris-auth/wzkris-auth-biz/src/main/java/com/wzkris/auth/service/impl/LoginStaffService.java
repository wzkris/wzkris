package com.wzkris.auth.service.impl;

import com.wzkris.auth.enums.BizLoginCode;
import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.auth.security.constants.OAuth2LoginTypeConstant;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.core.model.domain.LoginStaff;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.exception.CustomErrorCodes;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.common.web.utils.UserAgentUtil;
import com.wzkris.principal.feign.staff.StaffInfoFeign;
import com.wzkris.principal.feign.staff.req.QueryStaffPermsReq;
import com.wzkris.principal.feign.staff.resp.StaffInfoResp;
import com.wzkris.principal.feign.staff.resp.StaffPermissionResp;
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
public class LoginStaffService extends UserInfoTemplate {

    private final CaptchaService captchaService;

    private final StaffInfoFeign staffInfoFeign;

    private final PasswordEncoder passwordEncoder;

    @Nullable
    @Override
    public MyPrincipal loadUserByPhoneNumber(String phoneNumber) {
        StaffInfoResp staffResp = staffInfoFeign.getByPhoneNumber(phoneNumber);

        if (staffResp == null) {
            captchaService.freezeAccount(phoneNumber, 60);
            return null;
        }

        try {
            return this.buildLoginStaff(staffResp);
        } catch (Exception e) {
            this.recordFailedLog(staffResp, OAuth2LoginTypeConstant.SMS, e.getMessage());
            throw e;
        }
    }

    @Nullable
    @Override
    public MyPrincipal loadByUsernameAndPassword(String staffName, String password) throws UsernameNotFoundException {
        StaffInfoResp staffResp = staffInfoFeign.getByStaffName(staffName);

        if (staffResp == null) {
            captchaService.freezeAccount(staffName, 60);
            return null;
        }

        try {
            if (!passwordEncoder.matches(password, staffResp.getPassword())) {
                OAuth2ExceptionUtil.throwErrorI18n(
                        BizBaseCode.REQUEST_ERROR.value(), CustomErrorCodes.VALIDATE_ERROR, "oauth2.passlogin.fail");
            }

            return this.buildLoginStaff(staffResp);
        } catch (Exception e) {
            this.recordFailedLog(staffResp, OAuth2LoginTypeConstant.PASSWORD, e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean checkAuthType(AuthType authType) {
        return AuthType.STAFF.equals(authType);
    }

    /**
     * 构建登录用户
     */
    private LoginStaff buildLoginStaff(StaffInfoResp staffResp) {
        // 校验用户状态
        this.checkAccount(staffResp);

        // 获取权限信息
        StaffPermissionResp permissions = staffInfoFeign.getPermission(
                new QueryStaffPermsReq(staffResp.getStaffId(), staffResp.getTenantId()));

        return new LoginStaff(staffResp.getStaffId(),
                new HashSet<>(permissions.getGrantedAuthority()),
                permissions.getAdmin(),
                staffResp.getStaffName(),
                staffResp.getTenantId());
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(StaffInfoResp staffResp) {
        if (StringUtil.equals(staffResp.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCode.USER_DISABLED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        } else if (StringUtil.equals(staffResp.getTenantStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCode.TENANT_DISABLED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.tenant.disabled");
        } else if (staffResp.getTenantExpired().getTime() < System.currentTimeMillis()) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCode.TENANT_EXPIRED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.tenant.expired");
        } else if (StringUtil.equals(staffResp.getPackageStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCode.TENANT_PACKAGE_EXPIRED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.package.disabled");
        }
    }

    /**
     * 记录失败日志
     */
    private void recordFailedLog(StaffInfoResp staffResp, String loginType, String errorMsg) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        LoginStaff loginStaff = new LoginStaff(staffResp.getStaffId(),
                Collections.emptySet(),
                false,
                staffResp.getStaffName(),
                staffResp.getTenantId());

        SpringUtil.getContext()
                .publishEvent(new LoginEvent(
                        loginStaff,
                        null,
                        loginType,
                        false,
                        errorMsg,
                        ServletUtil.getClientIP(request),
                        UserAgentUtil.INSTANCE.parse(request.getHeader(HttpHeaders.USER_AGENT))));
    }

}

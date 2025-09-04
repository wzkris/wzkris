package com.wzkris.auth.service.impl;

import com.wzkris.auth.enums.BizLoginCode;
import com.wzkris.auth.rmi.domain.LoginCustomer;
import com.wzkris.auth.rmi.enums.AuthenticatedType;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.captcha.service.CaptchaService;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.user.rmi.CustomerFeign;
import com.wzkris.user.rmi.domain.resp.CustomerResp;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginCustomerService extends UserInfoTemplate {

    private final CaptchaService captchaService;

    private final CustomerFeign customerFeign;

    @Nullable
    @Override
    public LoginCustomer loadUserByPhoneNumber(String phoneNumber) {
        CustomerResp userResp = customerFeign.getByPhoneNumber(phoneNumber);

        if (userResp == null) {
            captchaService.freezeAccount(phoneNumber, 60);
            return null;
        }

        return this.checkAndBuild(userResp);
    }

    @Nullable
    @Override
    public LoginCustomer loadUserByWechat(String identifierType, String wxCode) {
        CustomerResp userResp = customerFeign.getOrRegisterByIdentifier(identifierType, wxCode);

        if (userResp == null) {
            return null;
        }
        return this.checkAndBuild(userResp);
    }

    @Override
    public boolean checkAuthenticatedType(AuthenticatedType authenticatedType) {
        return AuthenticatedType.CUSTOMER.equals(authenticatedType);
    }

    /**
     * 构建登录用户
     */
    private LoginCustomer checkAndBuild(CustomerResp userResp) {
        // 校验用户状态
        this.checkAccount(userResp);

        LoginCustomer loginCustomer = new LoginCustomer(userResp.getCustomerId(), null);
        loginCustomer.setPhoneNumber(userResp.getPhoneNumber());

        return loginCustomer;
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(CustomerResp customerResp) {
        if (StringUtil.equals(customerResp.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCode.USER_DISABLED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }

}

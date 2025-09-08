package com.wzkris.auth.service.impl;

import com.wzkris.auth.enums.BizLoginCode;
import com.wzkris.auth.feign.domain.LoginCustomer;
import com.wzkris.auth.feign.enums.AuthType;
import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.auth.security.constants.OAuth2LoginTypeConstant;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.captcha.service.CapService;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.common.web.utils.UserAgentUtil;
import com.wzkris.user.feign.customer.CustomerFeign;
import com.wzkris.user.feign.customer.resp.CustomerResp;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginCustomerService extends UserInfoTemplate {

    private final CapService capService;

    private final CustomerFeign customerFeign;

    @Nullable
    @Override
    public LoginCustomer loadUserByPhoneNumber(String phoneNumber) {
        CustomerResp customerResp = customerFeign.getByPhoneNumber(phoneNumber);

        if (customerResp == null) {
            capService.freezeAccount(phoneNumber, 60);
            return null;
        }

        try {
            return this.buildLoginCustomer(customerResp);
        } catch (Exception e) {
            this.recordFailedLog(customerResp, OAuth2LoginTypeConstant.SMS, e.getMessage());
            throw e;
        }
    }

    @Nullable
    @Override
    public LoginCustomer loadUserByWechat(String identifierType, String wxCode) {
        CustomerResp customerResp = customerFeign.getOrRegisterByIdentifier(identifierType, wxCode);

        if (customerResp == null) {
            return null;
        }

        try {
            return this.buildLoginCustomer(customerResp);
        } catch (Exception e) {
            this.recordFailedLog(customerResp, OAuth2LoginTypeConstant.WECHAT, e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean checkAuthType(AuthType authType) {
        return AuthType.CUSTOMER.equals(authType);
    }

    /**
     * 构建登录用户
     */
    private LoginCustomer buildLoginCustomer(CustomerResp customerResp) {
        // 校验用户状态
        this.checkAccount(customerResp);

        LoginCustomer loginCustomer = new LoginCustomer(customerResp.getCustomerId());
        loginCustomer.setPhoneNumber(customerResp.getPhoneNumber());

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

    private void recordFailedLog(CustomerResp customerResp, String loginType, String errorMsg) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        LoginCustomer customer = new LoginCustomer(customerResp.getCustomerId());
        SpringUtil.getContext()
                .publishEvent(new LoginEvent(
                        customer,
                        null,
                        loginType,
                        CommonConstants.FAIL,
                        errorMsg,
                        ServletUtil.getClientIP(request),
                        UserAgentUtil.INSTANCE.parse(request.getHeader(HttpHeaders.USER_AGENT))));
    }

}

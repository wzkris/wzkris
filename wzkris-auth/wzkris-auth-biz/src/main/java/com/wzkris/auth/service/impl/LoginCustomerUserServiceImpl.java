package com.wzkris.auth.service.impl;

import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.event.LoginEvent;
import com.wzkris.auth.remote.interfaces.common.request.StringValueRequest;
import com.wzkris.auth.remote.interfaces.customer.ICustomerInfoRemote;
import com.wzkris.auth.remote.interfaces.customer.request.WexcxLoginRequest;
import com.wzkris.auth.remote.interfaces.customer.response.CustomerResponse;
import com.wzkris.auth.service.LoginUserService;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.*;
import com.wzkris.common.security.model.CustomerLoginUser;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginCustomerUserServiceImpl implements LoginUserService {

    private final ICustomerInfoRemote customerInfoRemote;

    @Nullable
    @Override
    public UsernamePasswordAuthenticationToken loadUserByPhoneNumber(String phoneNumber) {
        Result<CustomerResponse> customerResult = customerInfoRemote.queryByPhoneNumber(new StringValueRequest(phoneNumber));

        if (!ResultUtil.check(customerResult)) {
            return null;
        }
        CustomerResponse CustomerResponse = customerResult.getData();

        try {
            return this.buildAuthenticationToken(CustomerResponse);
        } catch (Exception e) {
            this.recordFailedLog(CustomerResponse, LoginTypeEnum.SMS.getValue(), e.getMessage());
            throw e;
        }
    }

    @Nullable
    @Override
    public UsernamePasswordAuthenticationToken loadUserByWxXcx(String wxCode, String phoneCode) {
        WexcxLoginRequest wexcxLoginRequest = new WexcxLoginRequest();
        wexcxLoginRequest.setWxCode(wxCode);
        wexcxLoginRequest.setPhoneCode(phoneCode);
        Result<CustomerResponse> customerResult = customerInfoRemote.wexcxLogin(wexcxLoginRequest);

        if (!ResultUtil.check(customerResult)) {
            return null;
        }
        CustomerResponse CustomerResponse = customerResult.getData();

        try {
            return this.buildAuthenticationToken(CustomerResponse);
        } catch (Exception e) {
            this.recordFailedLog(CustomerResponse, LoginTypeEnum.WE_XCX.getValue(), e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean checkAuthType(AuthTypeEnum authType) {
        return AuthTypeEnum.CUSTOMER.equals(authType);
    }

    /**
     * 构建认证Token
     */
    private UsernamePasswordAuthenticationToken buildAuthenticationToken(CustomerResponse customerResponse) {
        // 校验用户状态
        this.checkAccount(customerResponse);

        CustomerLoginUser loginUser = new CustomerLoginUser();
        loginUser.setUid(customerResponse.getCustomerId());
        loginUser.setAuthType(AuthTypeEnum.CUSTOMER);
        loginUser.setPhoneNumber(customerResponse.getPhoneNumber());

        // Customer 用户没有权限，使用空集合
        return UsernamePasswordAuthenticationToken.authenticated(
                loginUser, null, AuthorityUtils.createAuthorityList(Collections.emptySet()));
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(CustomerResponse CustomerResponse) {
        if (StringUtil.equals(CustomerResponse.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.USER_DISABLED.getCode(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }

    private void recordFailedLog(CustomerResponse CustomerResponse, String loginType, String errorMsg) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        CustomerLoginUser loginUser = new CustomerLoginUser();
        loginUser.setUid(CustomerResponse.getCustomerId());
        loginUser.setAuthType(AuthTypeEnum.CUSTOMER);
        loginUser.setPhoneNumber(CustomerResponse.getPhoneNumber());

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


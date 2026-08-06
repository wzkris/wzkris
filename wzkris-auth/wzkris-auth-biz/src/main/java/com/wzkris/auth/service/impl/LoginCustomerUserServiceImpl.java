package com.wzkris.auth.service.impl;

import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.event.LoginEvent;
import com.wzkris.auth.remote.interfaces.customer.ICustomerRemote;
import com.wzkris.auth.remote.interfaces.customer.request.CustomerQueryRequest;
import com.wzkris.auth.remote.interfaces.customer.request.WexcxLoginRequest;
import com.wzkris.auth.remote.interfaces.customer.response.CustomerQueryResponse;
import com.wzkris.auth.remote.interfaces.customer.response.CustomerListResponse;
import com.wzkris.auth.service.LoginUserService;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.DefaultLoginUser;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.*;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginCustomerUserServiceImpl implements LoginUserService {

    private final ICustomerRemote customerRemote;

    @Nullable
    @Override
    public UsernamePasswordAuthenticationToken loadUserByPhoneNumber(String phoneNumber) {
        CustomerQueryRequest request = new CustomerQueryRequest();
        request.setPhoneNumber(phoneNumber);
        Result<List<CustomerListResponse>> listResult = customerRemote.queryList(request);

        if (!ResultUtil.check(listResult)) {
            return null;
        }

        if (CollectionUtils.isEmpty(listResult.getData()) || listResult.getData().size() > 1) {
            return null;
        }

        CustomerListResponse customerResponse = listResult.getData().getFirst();

        try {
            return this.buildAuthenticationToken(customerResponse);
        } catch (Exception e) {
            this.recordFailedLog(customerResponse, LoginTypeEnum.SMS.getValue(), e.getMessage());
            throw e;
        }
    }

    @Nullable
    @Override
    public UsernamePasswordAuthenticationToken loadUserByWxXcx(String wxCode, String phoneCode) {
        WexcxLoginRequest wexcxLoginRequest = new WexcxLoginRequest();
        wexcxLoginRequest.setWxCode(wxCode);
        wexcxLoginRequest.setPhoneCode(phoneCode);
        Result<CustomerQueryResponse> customerResult = customerRemote.wexcxLogin(wexcxLoginRequest);

        if (!ResultUtil.check(customerResult)) {
            return null;
        }
        CustomerQueryResponse customerResponse = customerResult.getData();

        try {
            return this.buildAuthenticationToken(customerResponse);
        } catch (Exception e) {
            this.recordFailedLog(customerResponse, LoginTypeEnum.WE_XCX.getValue(), e.getMessage());
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
    private UsernamePasswordAuthenticationToken buildAuthenticationToken(CustomerQueryResponse customerResponse) {
        // 校验用户状态
        this.checkAccount(customerResponse);

        DefaultLoginUser loginUser = new DefaultLoginUser();
        loginUser.setUid(customerResponse.getId());
        loginUser.setAuthType(AuthTypeEnum.CUSTOMER);
        loginUser.setName(String.valueOf(customerResponse.getId()));

        // Customer 用户没有权限，使用空集合
        return UsernamePasswordAuthenticationToken.authenticated(
                loginUser, null, AuthorityUtils.createAuthorityList(Collections.emptySet()));
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(CustomerQueryResponse customerResponse) {
        if (StringUtil.equals(customerResponse.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.USER_DISABLED.getCode(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }

    private void recordFailedLog(CustomerQueryResponse customerResponse, String loginType, String errorMsg) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        DefaultLoginUser loginUser = new DefaultLoginUser();
        loginUser.setUid(customerResponse.getId());
        loginUser.setAuthType(AuthTypeEnum.CUSTOMER);
        loginUser.setName(String.valueOf(customerResponse.getId()));

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

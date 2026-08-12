package com.wzkris.auth.service.impl;

import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.enums.SocialTypeEnum;
import com.wzkris.auth.event.LoginEvent;
import com.wzkris.auth.remote.interfaces.customer.ICustomerRemote;
import com.wzkris.auth.remote.interfaces.customer.request.CustomerQueryRequest;
import com.wzkris.auth.remote.interfaces.customer.request.CustomerSocialUpdateRequest;
import com.wzkris.auth.remote.interfaces.customer.request.SocialLoginRequest;
import com.wzkris.auth.remote.interfaces.customer.response.CustomerQueryResponse;
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
    public UsernamePasswordAuthenticationToken loadUserByPhoneNumber(String phoneNumber, @Nullable String wxCode, @Nullable String appid) {
        CustomerQueryRequest request = new CustomerQueryRequest();
        request.setPhoneNumber(phoneNumber);
        Result<List<CustomerQueryResponse>> listResult = customerRemote.queryList(request);

        if (!ResultUtil.check(listResult)) {
            return null;
        }

        if (CollectionUtils.isEmpty(listResult.getData()) || listResult.getData().size() > 1) {
            return null;
        }

        CustomerQueryResponse customerResponse = listResult.getData().getFirst();

        try {
            // 手机号登录时绑定当前微信openid，用于后续微信支付
            if (StringUtil.isNotBlank(wxCode)) {
                this.bindWeXcxOpenid(customerResponse.getId(), wxCode, appid);
            }
            return this.buildAuthenticationToken(customerResponse);
        } catch (Exception e) {
            this.recordFailedLog(customerResponse, LoginTypeEnum.SMS.getValue(), e.getMessage());
            throw e;
        }
    }

    /**
     * 绑定当前微信openid到客户，失败仅记录日志不阻断登录
     */
    private void bindWeXcxOpenid(Long customerId, String wxCode, String appid) {
        try {
            CustomerSocialUpdateRequest bindRequest = new CustomerSocialUpdateRequest();
            bindRequest.setCustomerId(customerId);
            bindRequest.setSocialType(SocialTypeEnum.WE_XCX.getValue());
            bindRequest.setWxCode(wxCode);
            bindRequest.setAppid(appid);
            Result<Void> bindResult = customerRemote.updateSocialInfo(bindRequest);
            if (!ResultUtil.check(bindResult)) {
                log.warn("手机号登录绑定微信openid失败: {}", bindResult.getMessage());
            }
        } catch (Exception e) {
            log.warn("手机号登录绑定微信openid异常", e);
        }
    }

    @Nullable
    @Override
    public UsernamePasswordAuthenticationToken loadUserBySocial(String socialType, String wxCode, @Nullable String phoneCode, @Nullable String appid) {
        SocialLoginRequest socialLoginRequest = new SocialLoginRequest();
        socialLoginRequest.setSocialType(socialType);
        socialLoginRequest.setWxCode(wxCode);
        socialLoginRequest.setPhoneCode(phoneCode);
        socialLoginRequest.setAppid(appid);
        Result<CustomerQueryResponse> customerResult = customerRemote.socialLogin(socialLoginRequest);

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

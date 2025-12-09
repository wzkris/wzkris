package com.wzkris.auth.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.enums.BizCallCodeEnum;
import com.wzkris.common.core.model.domain.LoginCustomer;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.exception.CustomOAuth2Error;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import com.wzkris.common.web.utils.UserAgentUtil;
import com.wzkris.principal.httpservice.customer.CustomerInfoHttpService;
import com.wzkris.principal.httpservice.customer.req.WexcxLoginReq;
import com.wzkris.principal.httpservice.customer.resp.CustomerResp;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginCustomerService extends UserInfoTemplate {

    private final CaptchaService captchaService;

    private final CustomerInfoHttpService customerInfoHttpService;

    @Autowired
    @Lazy
    private WxMaService wxMaService;

    @Autowired
    @Lazy
    private WxMpService wxMpService;

    @Nullable
    @Override
    public LoginCustomer loadUserByPhoneNumber(String phoneNumber) {
        CustomerResp customerResp = customerInfoHttpService.getByPhoneNumber(phoneNumber);

        if (customerResp == null) {
            captchaService.freezeAccount(phoneNumber, 60);
            return null;
        }

        try {
            return this.buildLoginCustomer(customerResp);
        } catch (Exception e) {
            this.recordFailedLog(customerResp, LoginTypeEnum.SMS.getValue(), e.getMessage());
            throw e;
        }
    }

    @Nullable
    @Override
    public LoginCustomer loadUserByWxXcx(String wxCode, String phoneCode) {
        String identifier;
        String phoneNumber = null;
        try {
            identifier = wxMaService
                    .getUserService()
                    .getSessionInfo(wxCode)
                    .getOpenid();
            if (StringUtil.isNotBlank(phoneCode)) {
                phoneNumber = wxMaService.getUserService()
                        .getPhoneNumber(phoneCode).getPhoneNumber();
            }
        } catch (WxErrorException e) {
            CustomOAuth2Error error = new CustomOAuth2Error(BizCallCodeEnum.WX_ERROR.value(), e.getError().getErrorMsg());
            throw new OAuth2AuthenticationException(error);
        }

        if (StringUtil.isAnyBlank(identifier)) {
            log.error("微信小程序登录api查询结果为null，登录失败");
            return null;
        }

        WexcxLoginReq wexcxLoginReq = new WexcxLoginReq();
        wexcxLoginReq.setIdentifier(identifier);
        wexcxLoginReq.setPhoneNumber(phoneNumber);
        CustomerResp customerResp = customerInfoHttpService.wexcxLogin(wexcxLoginReq);

        if (customerResp == null) {
            return null;
        }

        try {
            LoginCustomer loginCustomer = this.buildLoginCustomer(customerResp);
            loginCustomer.setWxopenid(identifier);
            return loginCustomer;
        } catch (Exception e) {
            this.recordFailedLog(customerResp, LoginTypeEnum.WE_XCX.getValue(), e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean checkAuthType(AuthTypeEnum authType) {
        return AuthTypeEnum.CUSTOMER.equals(authType);
    }

    /**
     * 构建登录用户
     */
    private LoginCustomer buildLoginCustomer(CustomerResp customerResp) {
        // 校验用户状态
        this.checkAccount(customerResp);

        LoginCustomer loginCustomer = new LoginCustomer(customerResp.getCustomerId(), Collections.emptySet());
        loginCustomer.setPhoneNumber(customerResp.getPhoneNumber());
        return loginCustomer;
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(CustomerResp customerResp) {
        if (StringUtil.equals(customerResp.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.USER_DISABLED.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }

    private void recordFailedLog(CustomerResp customerResp, String loginType, String errorMsg) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        LoginCustomer loginCustomer = new LoginCustomer(customerResp.getCustomerId(), Collections.emptySet());
        loginCustomer.setPhoneNumber(customerResp.getPhoneNumber());

        SpringUtil.getContext()
                .publishEvent(new LoginEvent(
                        loginCustomer,
                        loginType,
                        false,
                        errorMsg,
                        ServletUtil.getClientIP(request),
                        UserAgentUtil.INSTANCE.parse(request.getHeader(HttpHeaders.USER_AGENT))));
    }

}

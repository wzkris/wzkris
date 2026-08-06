package com.wzkris.auth.listener;

import com.wzkris.auth.event.LoginEvent;
import com.wzkris.auth.remote.interfaces.admin.IAdminRemote;
import com.wzkris.auth.remote.interfaces.admin.request.LoginInfoUpdateRequest;
import com.wzkris.auth.remote.interfaces.customer.ICustomerRemote;
import com.wzkris.auth.remote.interfaces.loginlog.ILoginLogRemote;
import com.wzkris.auth.remote.interfaces.loginlog.request.LoginLogEvent;
import com.wzkris.auth.remote.interfaces.member.IMemberRemote;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.support.LoginUser;
import com.wzkris.common.core.utils.IpUtil;
import com.wzkris.common.core.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Collections;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 登录事件监听
 * @date : 2023/8/28 10:05
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginEventListener {

    private final ILoginLogRemote loginLogRemote;

    private final IAdminRemote adminRemote;

    private final IMemberRemote memberRemote;

    private final ICustomerRemote customerRemote;

    @Async
    @EventListener
    public void loginEvent(LoginEvent event) {
        final LoginUser loginUser = event.getLoginUser();
        log.info("'{}' 发生登录事件", loginUser);

        AuthTypeEnum authType = loginUser.getAuthType();
        if (authType == AuthTypeEnum.ADMIN || authType == AuthTypeEnum.TENANT || authType == AuthTypeEnum.CUSTOMER) {
            this.handleLogin(event, loginUser);
        }
    }

    private void handleLogin(LoginEvent event, LoginUser loginUser) {
        final String loginType = event.getLoginType();
        final String errorMsg = event.getErrorMsg();
        String ipAddr = event.getIpAddr();
        String userAgentText = event.getUserAgentText();
        String loginLocation = IpUtil.parseIp(ipAddr);
        String traceId = event.getTraceId();
        OffsetDateTime now = OffsetDateTime.now();

        updateLoginInfoIfSuccess(loginUser, ipAddr, event.getSuccess(), now);

        LoginLogEvent loginLogEvent = new LoginLogEvent();
        loginLogEvent.setAuthType(loginUser.getAuthType());
        loginLogEvent.setOperatorId(loginUser.getUid());
        loginLogEvent.setUsername(loginUser.getName());
        loginLogEvent.setTenantId(loginUser.getTenantId());
        loginLogEvent.setLoginTime(now);
        loginLogEvent.setLoginIp(ipAddr);
        loginLogEvent.setLoginType(loginType);
        loginLogEvent.setSuccess(event.getSuccess());
        loginLogEvent.setErrorMsg(errorMsg);
        loginLogEvent.setLoginLocation(loginLocation);
        loginLogEvent.setTraceId(traceId);
        loginLogEvent.setUserAgent(userAgentText);
        loginLogRemote.save(Collections.singletonList(loginLogEvent));
    }

    private void updateLoginInfoIfSuccess(LoginUser loginUser, String ipAddr, Boolean success, OffsetDateTime loginDate) {
        if (!Boolean.TRUE.equals(success)) {
            return;
        }
        LoginInfoUpdateRequest LoginInfoUpdateRequest = new LoginInfoUpdateRequest(loginUser.getUid());
        LoginInfoUpdateRequest.setLoginIp(ipAddr);
        LoginInfoUpdateRequest.setLoginDate(loginDate);
        AuthTypeEnum authType = loginUser.getAuthType();
        if (authType == AuthTypeEnum.ADMIN) {
            ResultUtil.checkNoData(adminRemote.updateLoginInfo(LoginInfoUpdateRequest));
        } else if (authType == AuthTypeEnum.TENANT) {
            ResultUtil.checkNoData(memberRemote.updateLoginInfo(LoginInfoUpdateRequest));
        } else if (authType == AuthTypeEnum.CUSTOMER) {
            ResultUtil.checkNoData(customerRemote.updateLoginInfo(LoginInfoUpdateRequest));
        }
    }

}


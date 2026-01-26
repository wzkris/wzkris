package com.wzkris.auth.listener;

import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.LoginUser;
import com.wzkris.common.core.utils.IpUtil;
import com.wzkris.system.httpservice.loginlog.LoginLogHttpService;
import com.wzkris.system.httpservice.loginlog.req.LoginLogEvent;
import com.wzkris.usercenter.httpservice.admin.AdminInfoHttpService;
import com.wzkris.usercenter.httpservice.admin.req.LoginInfoReq;
import com.wzkris.usercenter.httpservice.customer.CustomerInfoHttpService;
import com.wzkris.usercenter.httpservice.member.MemberInfoHttpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;

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

    private final TokenService tokenService;

    private final LoginLogHttpService loginLogHttpService;

    private final AdminInfoHttpService adminInfoHttpService;

    private final MemberInfoHttpService memberInfoHttpService;

    private final CustomerInfoHttpService customerInfoHttpService;

    @Async
    @EventListener
    public void loginEvent(LoginEvent event) {
        final LoginUser loginUser = event.getLoginUser();
        log.info("'{}' 发生登录事件", loginUser);

        AuthTypeEnum authType = loginUser.getAuthType();
        if (authType == AuthTypeEnum.ADMIN) {
            this.handleLoginAdmin(event, loginUser);
        } else if (authType == AuthTypeEnum.TENANT) {
            this.handleLoginTenant(event, loginUser);
        } else if (authType == AuthTypeEnum.CUSTOMER) {
            this.handleLoginCustomer(event, loginUser);
        }
    }

    private void handleLoginAdmin(LoginEvent event, LoginUser admin) {
        final String loginType = event.getLoginType();
        final String errorMsg = event.getErrorMsg();
        final String ipAddr = event.getIpAddr();
        final UserAgent userAgent = event.getUserAgent();

        // 获取客户端浏览器
        String browser = userAgent.getValue(UserAgent.AGENT_NAME);
        // 获取登录地址
        String loginLocation = IpUtil.parseIp(ipAddr);

        if (event.getSuccess()) {
            LoginInfoReq loginInfoReq = new LoginInfoReq(admin.getUid());
            loginInfoReq.setLoginIp(ipAddr);
            loginInfoReq.setLoginDate(new Date());
            adminInfoHttpService.updateLoginInfo(loginInfoReq);
        }
        // 插入后台登陆日志
        LoginLogEvent loginLogEvent = new LoginLogEvent();
        loginLogEvent.setAuthType(AuthTypeEnum.ADMIN.getValue());
        loginLogEvent.setOperatorId(admin.getUid());
        loginLogEvent.setUsername(admin.getUsername());
        loginLogEvent.setLoginTime(new Date());
        loginLogEvent.setLoginIp(ipAddr);
        loginLogEvent.setLoginType(loginType);
        loginLogEvent.setSuccess(event.getSuccess());
        loginLogEvent.setErrorMsg(errorMsg);
        loginLogEvent.setLoginLocation(loginLocation);
        loginLogEvent.setOs(userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME));
        loginLogEvent.setBrowser(browser);
        loginLogHttpService.save(Collections.singletonList(loginLogEvent));
    }

    private void handleLoginTenant(LoginEvent event, LoginUser tenant) {
        final String loginType = event.getLoginType();
        final String errorMsg = event.getErrorMsg();
        final String ipAddr = event.getIpAddr();
        final UserAgent userAgent = event.getUserAgent();

        // 获取客户端浏览器
        String browser = userAgent.getValue(UserAgent.AGENT_NAME);
        // 获取登录地址
        String loginLocation = IpUtil.parseIp(ipAddr);

        if (event.getSuccess()) {
            LoginInfoReq loginInfoReq = new LoginInfoReq(tenant.getUid());
            loginInfoReq.setLoginIp(ipAddr);
            loginInfoReq.setLoginDate(new Date());
            memberInfoHttpService.updateLoginInfo(loginInfoReq);
        }
        // 插入租户登陆日志
        LoginLogEvent loginLogEvent = new LoginLogEvent();
        loginLogEvent.setAuthType(AuthTypeEnum.TENANT.getValue());
        loginLogEvent.setOperatorId(tenant.getUid());
        loginLogEvent.setUsername(tenant.getUsername());
        loginLogEvent.setTenantId(tenant.getTenantId());
        loginLogEvent.setLoginTime(new Date());
        loginLogEvent.setLoginIp(ipAddr);
        loginLogEvent.setLoginType(loginType);
        loginLogEvent.setSuccess(event.getSuccess());
        loginLogEvent.setErrorMsg(errorMsg);
        loginLogEvent.setLoginLocation(loginLocation);
        loginLogEvent.setOs(userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME));
        loginLogEvent.setBrowser(browser);
        loginLogHttpService.save(Collections.singletonList(loginLogEvent));
    }

    private void handleLoginCustomer(LoginEvent event, LoginUser customer) {
        if (event.getSuccess()) {
            LoginInfoReq loginInfoReq = new LoginInfoReq(customer.getUid());
            loginInfoReq.setLoginIp(event.getIpAddr());
            loginInfoReq.setLoginDate(new Date());
            customerInfoHttpService.updateLoginInfo(loginInfoReq);
        }
    }

}

package com.wzkris.auth.listener;

import com.wzkris.auth.domain.OnlineSession;
import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.core.model.domain.LoginAdmin;
import com.wzkris.common.core.model.domain.LoginCustomer;
import com.wzkris.common.core.model.domain.LoginTenant;
import com.wzkris.common.core.utils.IpUtil;
import com.wzkris.message.feign.loginlog.LoginLogFeign;
import com.wzkris.message.feign.loginlog.req.LoginLogEvent;
import com.wzkris.principal.feign.admin.AdminInfoFeign;
import com.wzkris.principal.feign.admin.req.LoginInfoReq;
import com.wzkris.principal.feign.customer.CustomerInfoFeign;
import com.wzkris.principal.feign.member.MemberInfoFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.Objects;

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

    private final LoginLogFeign loginLogFeign;

    private final AdminInfoFeign adminInfoFeign;

    private final MemberInfoFeign memberInfoFeign;

    private final CustomerInfoFeign customerInfoFeign;

    @Async
    @EventListener
    public void loginEvent(LoginEvent event) {
        final MyPrincipal principal = event.getPrincipal();
        log.info("'{}' 发生登录事件", principal);

        if (Objects.equals(principal.getType(), AuthTypeEnum.ADMIN)) {
            this.handleLoginAdmin(event, (LoginAdmin) principal);
        } else if (Objects.equals(principal.getType(), AuthTypeEnum.TENANT)) {
            this.handleLoginTenant(event, (LoginTenant) principal);
        } else if (Objects.equals(principal.getType(), AuthTypeEnum.CUSTOMER)) {
            this.handleLoginCustomer(event, (LoginCustomer) principal);
        }
    }

    private void handleLoginAdmin(LoginEvent event, LoginAdmin admin) {
        final String loginType = event.getLoginType();
        final String errorMsg = event.getErrorMsg();
        final String ipAddr = event.getIpAddr();
        final UserAgent userAgent = event.getUserAgent();

        // 获取客户端浏览器
        String browser = userAgent.getValue(UserAgent.AGENT_NAME);
        // 获取登录地址
        String loginLocation = IpUtil.parseIp(ipAddr);

        if (event.getSuccess()) { // 更新用户登录信息、在线会话信息
            OnlineSession onlineSession = new OnlineSession();
            onlineSession.setDeviceType(userAgent.getValue(UserAgent.DEVICE_NAME));
            onlineSession.setLoginIp(ipAddr);
            onlineSession.setLoginLocation(loginLocation);
            onlineSession.setBrowser(browser);
            onlineSession.setOs(userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME));
            onlineSession.setLoginTime(new Date());

            tokenService.putSession(admin.getType().getValue(), admin.getId(), event.getRefreshToken(), onlineSession);

            LoginInfoReq loginInfoReq = new LoginInfoReq(admin.getId());
            loginInfoReq.setLoginIp(ipAddr);
            loginInfoReq.setLoginDate(new Date());
            adminInfoFeign.updateLoginInfo(loginInfoReq);
        }
        // 插入后台登陆日志
        LoginLogEvent loginLogEvent = new LoginLogEvent();
        loginLogEvent.setAuthType(AuthTypeEnum.ADMIN.getValue());
        loginLogEvent.setOperatorId(admin.getId());
        loginLogEvent.setUsername(admin.getUsername());
        loginLogEvent.setLoginTime(new Date());
        loginLogEvent.setLoginIp(ipAddr);
        loginLogEvent.setLoginType(loginType);
        loginLogEvent.setSuccess(event.getSuccess());
        loginLogEvent.setErrorMsg(errorMsg);
        loginLogEvent.setLoginLocation(loginLocation);
        loginLogEvent.setOs(userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME));
        loginLogEvent.setBrowser(browser);
        loginLogFeign.save(Collections.singletonList(loginLogEvent));
    }

    private void handleLoginTenant(LoginEvent event, LoginTenant tenant) {
        final String loginType = event.getLoginType();
        final String errorMsg = event.getErrorMsg();
        final String ipAddr = event.getIpAddr();
        final UserAgent userAgent = event.getUserAgent();

        // 获取客户端浏览器
        String browser = userAgent.getValue(UserAgent.AGENT_NAME);
        // 获取登录地址
        String loginLocation = IpUtil.parseIp(ipAddr);

        if (event.getSuccess()) { // 更新用户登录信息、在线会话信息
            OnlineSession onlineSession = new OnlineSession();
            onlineSession.setDeviceType(userAgent.getValue(UserAgent.DEVICE_NAME));
            onlineSession.setLoginIp(ipAddr);
            onlineSession.setLoginLocation(loginLocation);
            onlineSession.setBrowser(browser);
            onlineSession.setOs(userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME));
            onlineSession.setLoginTime(new Date());

            tokenService.putSession(tenant.getType().getValue(), tenant.getId(), event.getRefreshToken(), onlineSession);

            LoginInfoReq loginInfoReq = new LoginInfoReq(tenant.getId());
            loginInfoReq.setLoginIp(ipAddr);
            loginInfoReq.setLoginDate(new Date());
            memberInfoFeign.updateLoginInfo(loginInfoReq);
        }
        // 插入租户登陆日志
        LoginLogEvent loginLogEvent = new LoginLogEvent();
        loginLogEvent.setAuthType(AuthTypeEnum.TENANT.getValue());
        loginLogEvent.setOperatorId(tenant.getId());
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
        loginLogFeign.save(Collections.singletonList(loginLogEvent));
    }

    private void handleLoginCustomer(LoginEvent event, LoginCustomer customer) {
        final String ipAddr = event.getIpAddr();
        final UserAgent userAgent = event.getUserAgent();

        // 获取客户端浏览器
        String browser = userAgent.getValue(UserAgent.AGENT_NAME);
        // 获取登录地址
        String loginLocation = IpUtil.parseIp(ipAddr);
        if (event.getSuccess()) { // 更新用户登录信息
            OnlineSession onlineSession = new OnlineSession();
            onlineSession.setDeviceType(userAgent.getValue(UserAgent.DEVICE_NAME));
            onlineSession.setLoginIp(ipAddr);
            onlineSession.setLoginLocation(loginLocation);
            onlineSession.setBrowser(browser);
            onlineSession.setOs(userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME));
            onlineSession.setLoginTime(new Date());

            tokenService.putSession(customer.getType().getValue(), customer.getId(), event.getRefreshToken(), onlineSession);

            LoginInfoReq loginInfoReq = new LoginInfoReq(customer.getId());
            loginInfoReq.setLoginIp(event.getIpAddr());
            loginInfoReq.setLoginDate(new Date());
            customerInfoFeign.updateLoginInfo(loginInfoReq);
        }
    }

}

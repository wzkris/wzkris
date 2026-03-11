package com.wzkris.auth.listener;

import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.utils.IpUtil;
import com.wzkris.common.core.utils.ResultUtil;
import com.wzkris.common.security.model.AdminLoginUser;
import com.wzkris.common.security.model.TenantLoginUser;
import com.wzkris.system.httpclient.loginlog.LoginLogClient;
import com.wzkris.system.httpclient.loginlog.req.LoginLogEvent;
import com.wzkris.usercenter.httpclient.admin.AdminInfoClient;
import com.wzkris.usercenter.httpclient.admin.req.LoginInfoReq;
import com.wzkris.usercenter.httpclient.customer.CustomerInfoClient;
import com.wzkris.usercenter.httpclient.member.MemberInfoClient;
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

    private final LoginLogClient loginLogClient;

    private final AdminInfoClient adminInfoClient;

    private final MemberInfoClient memberInfoClient;

    private final CustomerInfoClient customerInfoClient;

    @Async
    @EventListener
    public void loginEvent(LoginEvent event) {
        final BaseLoginUser loginUser = event.getLoginUser();
        log.info("'{}' 发生登录事件", loginUser);

        AuthTypeEnum authType = loginUser.getAuthType();
        if (authType == AuthTypeEnum.ADMIN && loginUser instanceof AdminLoginUser admin) {
            this.handleLoginAdmin(event, admin);
        } else if (authType == AuthTypeEnum.TENANT && loginUser instanceof TenantLoginUser tenant) {
            this.handleLoginTenant(event, tenant);
        } else if (authType == AuthTypeEnum.CUSTOMER) {
            this.handleLoginCustomer(event, loginUser);
        }
    }

    private void handleLoginAdmin(LoginEvent event, AdminLoginUser admin) {
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
            if (!ResultUtil.checkNoData(adminInfoClient.updateLoginInfo(loginInfoReq))) {
                log.warn("更新管理员登录信息失败: {}", loginInfoReq);
            }
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
        if (!ResultUtil.checkNoData(loginLogClient.save(Collections.singletonList(loginLogEvent)))) {
            log.warn("记录管理员登录日志失败: {}", loginLogEvent);
        }
    }

    private void handleLoginTenant(LoginEvent event, TenantLoginUser tenant) {
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
            if (!ResultUtil.checkNoData(memberInfoClient.updateLoginInfo(loginInfoReq))) {
                log.warn("更新租户成员登录信息失败: {}", loginInfoReq);
            }
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
        if (!ResultUtil.checkNoData(loginLogClient.save(Collections.singletonList(loginLogEvent)))) {
            log.warn("记录租户成员登录日志失败: {}", loginLogEvent);
        }
    }

    private void handleLoginCustomer(LoginEvent event, BaseLoginUser customer) {
        if (event.getSuccess()) {
            LoginInfoReq loginInfoReq = new LoginInfoReq(customer.getUid());
            loginInfoReq.setLoginIp(event.getIpAddr());
            loginInfoReq.setLoginDate(new Date());
            if (!ResultUtil.checkNoData(customerInfoClient.updateLoginInfo(loginInfoReq))) {
                log.warn("更新客户登录信息失败: {}", loginInfoReq);
            }
        }
    }

}

package com.wzkris.auth.listener;

import com.wzkris.auth.domain.OnlineUser;
import com.wzkris.auth.listener.event.LoginTokenEvent;
import com.wzkris.auth.rmi.domain.ClientUser;
import com.wzkris.auth.rmi.domain.SystemUser;
import com.wzkris.auth.rmi.enums.AuthenticatedType;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.core.utils.AddressUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.system.rmi.RmiSysLogFeign;
import com.wzkris.system.rmi.domain.req.LoginLogReq;
import com.wzkris.user.rmi.RmiAppUserFeign;
import com.wzkris.user.rmi.RmiSysUserFeign;
import com.wzkris.user.rmi.domain.req.LoginInfoReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
public class LoginTokenEventListener {

    private final TokenService tokenService;

    private final RmiSysLogFeign rmiSysLogFeign;

    private final RmiSysUserFeign rmiSysUserFeign;

    private final RmiAppUserFeign rmiAppUserFeign;

    @Async
    @EventListener
    public void loginTokenEvent(LoginTokenEvent event) {
        final CorePrincipal principal = event.getPrincipal();

        if (StringUtil.equals(principal.getType(), AuthenticatedType.SYSTEM_USER.getValue())) {
            this.handleSystemUser(event, (SystemUser) principal);
        } else if (StringUtil.equals(principal.getType(), AuthenticatedType.CLIENT_USER.getValue())) {
            this.handleClientUser(event, (ClientUser) principal);
        } else {
            log.warn("{} 发生登录事件, 忽略处理", principal);
        }
    }

    private void handleSystemUser(LoginTokenEvent event, SystemUser user) {
        final String loginType = event.getLoginType();
        final String status = event.getStatus();
        final String errorMsg = event.getErrorMsg();
        final String ipAddr = event.getIpAddr();
        final UserAgent userAgent = event.getUserAgent();

        boolean loginSuccess = status.equals(CommonConstants.STATUS_ENABLE);
        log.info("监听到用户’{}‘登录'{}'事件, 登录IP：{}", user.getName(), loginSuccess ? "成功" : "失败", ipAddr);

        // 获取客户端浏览器
        String browser = userAgent.getValue(UserAgent.AGENT_NAME);
        // 获取登录地址
        String loginLocation = AddressUtil.getRealAddressByIp(ipAddr);

        if (loginSuccess) { // 更新用户登录信息、在线会话信息
            OnlineUser onlineUser = new OnlineUser();
            onlineUser.setDeviceType(userAgent.getValue(UserAgent.DEVICE_NAME));
            onlineUser.setLoginIp(ipAddr);
            onlineUser.setLoginLocation(loginLocation);
            onlineUser.setBrowser(browser);
            onlineUser.setOs(userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME));
            onlineUser.setLoginTime(new Date());

            tokenService.putOnlineSession(user.getId(), event.getRefreshToken(), onlineUser);

            LoginInfoReq loginInfoReq = new LoginInfoReq(user.getUserId());
            loginInfoReq.setLoginIp(ipAddr);
            loginInfoReq.setLoginDate(new Date());
            rmiSysUserFeign.updateLoginInfo(loginInfoReq);
        }
        // 插入后台登陆日志
        final LoginLogReq loginLogReq = new LoginLogReq();
        loginLogReq.setUserId(user.getUserId());
        loginLogReq.setUsername(user.getUsername());
        loginLogReq.setTenantId(user.getTenantId());
        loginLogReq.setLoginTime(new Date());
        loginLogReq.setLoginIp(ipAddr);
        loginLogReq.setLoginType(loginType);
        loginLogReq.setStatus(status);
        loginLogReq.setErrorMsg(errorMsg);
        loginLogReq.setLoginLocation(loginLocation);
        loginLogReq.setOs(userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME));
        loginLogReq.setBrowser(browser);
        rmiSysLogFeign.saveLoginlog(loginLogReq);
    }

    private void handleClientUser(LoginTokenEvent event, ClientUser user) {
        final String status = event.getStatus();
        boolean loginSuccess = status.equals(CommonConstants.STATUS_ENABLE);

        if (loginSuccess) { // 更新用户登录信息
            LoginInfoReq loginInfoReq = new LoginInfoReq(user.getUserId());
            loginInfoReq.setLoginIp(event.getIpAddr());
            loginInfoReq.setLoginDate(new Date());
            rmiAppUserFeign.updateLoginInfo(loginInfoReq);
        }
    }

}

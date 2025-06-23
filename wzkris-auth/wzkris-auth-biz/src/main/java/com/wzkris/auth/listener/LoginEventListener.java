package com.wzkris.auth.listener;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.useragent.UserAgent;
import com.wzkris.auth.domain.OnlineUser;
import com.wzkris.auth.listener.event.LoginEvent;
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
public class LoginEventListener {

    private final TokenService tokenService;

    private final RmiSysLogFeign rmiSysLogFeign;

    private final RmiSysUserFeign rmiSysUserFeign;

    private final RmiAppUserFeign rmiAppUserFeign;

    @Async
    @EventListener
    public void loginEvent(LoginEvent event) {
        final CorePrincipal principal = event.getPrincipal();

        if (StringUtil.equals(principal.getType(), AuthenticatedType.SYSTEM_USER.getValue())) {
            this.handleSystemUser(event, (SystemUser) principal);
        } else if (StringUtil.equals(principal.getType(), AuthenticatedType.CLIENT_USER.getValue())) {
            this.handleClientUser(event, (ClientUser) principal);
        } else {
            log.warn("{} 发生登录事件, 忽略处理", principal);
        }
    }

    private void handleSystemUser(LoginEvent event, SystemUser user) {
        final String loginType = event.getLoginType();
        final String status = event.getStatus();
        final String errorMsg = event.getErrorMsg();
        final String ipAddr = event.getIpAddr();
        final UserAgent userAgent = event.getUserAgent();

        boolean loginSuccess = status.equals(CommonConstants.STATUS_ENABLE);
        log.info("监听到用户’{}‘登录'{}'事件, 登录IP：{}", user.getName(), loginSuccess ? "成功" : "失败", ipAddr);

        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        // 获取登录地址
        String loginLocation = AddressUtil.getRealAddressByIp(ipAddr);

        if (loginSuccess) { // 更新用户登录信息、在线会话信息
            OnlineUser onlineUser = new OnlineUser();
            onlineUser.setRefreshToken(event.getRefreshToken());
            onlineUser.setDeviceType(userAgent.getPlatform().getName());
            onlineUser.setLoginIp(ipAddr);
            onlineUser.setLoginLocation(loginLocation);
            onlineUser.setBrowser(browser);
            onlineUser.setOs(userAgent.getOs().getName());
            onlineUser.setLoginTime(new Date());

            tokenService.putOnlineSession(user.getUserId(), onlineUser);

            LoginInfoReq loginInfoReq = new LoginInfoReq(user.getUserId());
            loginInfoReq.setLoginIp(ipAddr);
            loginInfoReq.setLoginDate(DateUtil.date());
            rmiSysUserFeign.updateLoginInfo(loginInfoReq);
        }
        // 插入后台登陆日志
        final LoginLogReq loginLogReq = new LoginLogReq();
        loginLogReq.setUserId(user.getUserId());
        loginLogReq.setUsername(user.getUsername());
        loginLogReq.setTenantId(user.getTenantId());
        loginLogReq.setLoginTime(DateUtil.date());
        loginLogReq.setLoginIp(ipAddr);
        loginLogReq.setLoginType(loginType);
        loginLogReq.setStatus(status);
        loginLogReq.setErrorMsg(errorMsg);
        loginLogReq.setLoginLocation(loginLocation);
        loginLogReq.setOs(userAgent.getOs().getName());
        loginLogReq.setBrowser(browser);
        rmiSysLogFeign.saveLoginlog(loginLogReq);
    }

    private void handleClientUser(LoginEvent event, ClientUser user) {
        final String status = event.getStatus();
        boolean loginSuccess = status.equals(CommonConstants.STATUS_ENABLE);

        if (loginSuccess) { // 更新用户登录信息
            LoginInfoReq loginInfoReq = new LoginInfoReq(user.getUserId());
            loginInfoReq.setLoginIp(event.getIpAddr());
            loginInfoReq.setLoginDate(DateUtil.date());
            rmiAppUserFeign.updateLoginInfo(loginInfoReq);
        }
    }

}

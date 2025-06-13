package com.wzkris.auth.listener;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.useragent.UserAgent;
import com.wzkris.auth.oauth2.config.TokenProperties;
import com.wzkris.auth.domain.OnlineUser;
import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.auth.utils.OnlineUserUtil;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.utils.AddressUtil;
import com.wzkris.common.security.oauth2.domain.AuthBaseUser;
import com.wzkris.common.security.oauth2.domain.model.ClientUser;
import com.wzkris.common.security.oauth2.domain.model.LoginUser;
import com.wzkris.system.rmi.RmiSysLogFeign;
import com.wzkris.system.rmi.domain.req.LoginLogReq;
import com.wzkris.user.rmi.RmiAppUserFeign;
import com.wzkris.user.rmi.RmiSysUserFeign;
import com.wzkris.user.rmi.domain.req.LoginInfoReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    private final TokenProperties tokenProperties;

    private final RmiSysLogFeign rmiSysLogFeign;

    private final RmiSysUserFeign rmiSysUserFeign;

    private final RmiAppUserFeign rmiAppUserFeign;

    @Async
    @EventListener
    public void loginEvent(LoginEvent event) {
        final AuthBaseUser baseUser = event.getUser();

        switch (baseUser.getLoginType()) {
            case SYSTEM_USER -> {
                this.handleSystemUser(event, (LoginUser) baseUser);
            }
            case CLIENT_USER -> {
                this.handleClientUser(event, (ClientUser) baseUser);
            }
            default -> log.warn("{} 发生登录事件, 忽略处理", baseUser);
        }
    }

    private void handleSystemUser(LoginEvent event, LoginUser loginUser) {
        final String grantType = event.getGrantType();
        final String status = event.getStatus();
        final String errorMsg = event.getErrorMsg();
        final String ipAddr = event.getIpAddr();
        final UserAgent userAgent = event.getUserAgent();

        boolean loginSuccess = status.equals(CommonConstants.STATUS_ENABLE);
        log.info("监听到用户’{}‘登录'{}'事件, 登录IP：{}", loginUser.getName(), loginSuccess ? "成功" : "失败", ipAddr);

        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        // 获取登录地址
        String loginLocation = AddressUtil.getRealAddressByIp(ipAddr);

        if (loginSuccess) { // 更新用户登录信息、在线会话信息
            OnlineUser onlineUser = new OnlineUser();
            onlineUser.setTokenId(event.getTokenId());
            onlineUser.setDeviceType(userAgent.getPlatform().getName());
            onlineUser.setLoginIp(ipAddr);
            onlineUser.setLoginLocation(loginLocation);
            onlineUser.setBrowser(browser);
            onlineUser.setOs(userAgent.getOs().getName());
            onlineUser.setLoginTime(new Date());

            RMapCache<String, OnlineUser> onlineCache = OnlineUserUtil.getOnlineCache(loginUser.getUserId());
            onlineCache.put(
                    onlineUser.getTokenId(), onlineUser, tokenProperties.getRefreshTokenTimeOut(), TimeUnit.SECONDS);

            LoginInfoReq loginInfoReq = new LoginInfoReq(loginUser.getUserId());
            loginInfoReq.setLoginIp(ipAddr);
            loginInfoReq.setLoginDate(DateUtil.date());
            rmiSysUserFeign.updateLoginInfo(loginInfoReq);
        }
        // 插入后台登陆日志
        final LoginLogReq loginLogReq = new LoginLogReq();
        loginLogReq.setUserId(loginUser.getUserId());
        loginLogReq.setUsername(loginUser.getUsername());
        loginLogReq.setTenantId(loginUser.getTenantId());
        loginLogReq.setLoginTime(DateUtil.date());
        loginLogReq.setLoginIp(ipAddr);
        loginLogReq.setGrantType(grantType);
        loginLogReq.setStatus(status);
        loginLogReq.setErrorMsg(errorMsg);
        loginLogReq.setLoginLocation(loginLocation);
        loginLogReq.setOs(userAgent.getOs().getName());
        loginLogReq.setBrowser(browser);
        rmiSysLogFeign.saveLoginlog(loginLogReq);
    }

    private void handleClientUser(LoginEvent event, ClientUser clientUser) {
        final String status = event.getStatus();
        boolean loginSuccess = status.equals(CommonConstants.STATUS_ENABLE);

        if (loginSuccess) { // 更新用户登录信息
            LoginInfoReq loginInfoReq = new LoginInfoReq(clientUser.getUserId());
            loginInfoReq.setLoginIp(event.getIpAddr());
            loginInfoReq.setLoginDate(DateUtil.date());
            rmiAppUserFeign.updateLoginInfo(loginInfoReq);
        }
    }

}

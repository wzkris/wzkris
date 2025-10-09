package com.wzkris.auth.listener;

import com.wzkris.auth.domain.OnlineUser;
import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.model.CorePrincipal;
import com.wzkris.common.core.model.domain.LoginCustomer;
import com.wzkris.common.core.model.domain.LoginUser;
import com.wzkris.common.core.utils.IpUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.system.feign.userlog.UserLogFeign;
import com.wzkris.system.feign.userlog.req.LoginLogReq;
import com.wzkris.user.feign.customer.CustomerFeign;
import com.wzkris.user.feign.userinfo.UserInfoFeign;
import com.wzkris.user.feign.userinfo.req.LoginInfoReq;
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
public class LoginEventListener {

    private final TokenService tokenService;

    private final UserLogFeign userLogFeign;

    private final UserInfoFeign userInfoFeign;

    private final CustomerFeign customerFeign;

    @Async
    @EventListener
    public void loginEvent(LoginEvent event) {
        final CorePrincipal principal = event.getPrincipal();
        log.info("'{}' 发生登录事件", principal);

        if (StringUtil.equals(principal.getType(), AuthType.USER.getValue())) {
            this.handleLoginUser(event, (LoginUser) principal);
        } else if (StringUtil.equals(principal.getType(), AuthType.CUSTOMER.getValue())) {
            this.handleLoginCustomer(event, (LoginCustomer) principal);
        }
    }

    private void handleLoginUser(LoginEvent event, LoginUser user) {
        final String loginType = event.getLoginType();
        final String status = event.getStatus();
        final String errorMsg = event.getErrorMsg();
        final String ipAddr = event.getIpAddr();
        final UserAgent userAgent = event.getUserAgent();

        boolean loginSuccess = status.equals(CommonConstants.SUCCESS);
        if (log.isDebugEnabled()) {
            log.debug("监听到用户’{}‘登录'{}'事件, 登录IP：{}", user.getName(), loginSuccess ? "成功" : "失败", ipAddr);
        }

        // 获取客户端浏览器
        String browser = userAgent.getValue(UserAgent.AGENT_NAME);
        // 获取登录地址
        String loginLocation = IpUtil.parseIp(ipAddr);

        if (loginSuccess) { // 更新用户登录信息、在线会话信息
            OnlineUser onlineUser = new OnlineUser();
            onlineUser.setDeviceType(userAgent.getValue(UserAgent.DEVICE_NAME));
            onlineUser.setLoginIp(ipAddr);
            onlineUser.setLoginLocation(loginLocation);
            onlineUser.setBrowser(browser);
            onlineUser.setOs(userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME));
            onlineUser.setLoginTime(new Date());

            tokenService.putOnlineSession(user.getId(), event.getRefreshToken(), onlineUser);

            LoginInfoReq loginInfoReq = new LoginInfoReq(user.getId());
            loginInfoReq.setLoginIp(ipAddr);
            loginInfoReq.setLoginDate(new Date());
            userInfoFeign.updateLoginInfo(loginInfoReq);
        }
        // 插入后台登陆日志
        final LoginLogReq loginLogReq = new LoginLogReq();
        loginLogReq.setUserId(user.getId());
        loginLogReq.setUsername(user.getUsername());
        loginLogReq.setLoginTime(new Date());
        loginLogReq.setLoginIp(ipAddr);
        loginLogReq.setLoginType(loginType);
        loginLogReq.setStatus(status);
        loginLogReq.setErrorMsg(errorMsg);
        loginLogReq.setLoginLocation(loginLocation);
        loginLogReq.setOs(userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME));
        loginLogReq.setBrowser(browser);
        userLogFeign.saveLoginlog(loginLogReq);
    }

    private void handleLoginCustomer(LoginEvent event, LoginCustomer user) {
        final String status = event.getStatus();
        boolean loginSuccess = status.equals(CommonConstants.SUCCESS);

        if (loginSuccess) { // 更新用户登录信息
            LoginInfoReq loginInfoReq = new LoginInfoReq(user.getId());
            loginInfoReq.setLoginIp(event.getIpAddr());
            loginInfoReq.setLoginDate(new Date());
            customerFeign.updateLoginInfo(loginInfoReq);
        }
    }

}

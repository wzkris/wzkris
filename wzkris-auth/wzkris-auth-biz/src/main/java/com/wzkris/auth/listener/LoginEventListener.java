package com.wzkris.auth.listener;

import com.wzkris.auth.domain.OnlineSession;
import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.core.model.domain.LoginCustomer;
import com.wzkris.common.core.model.domain.LoginStaff;
import com.wzkris.common.core.model.domain.LoginUser;
import com.wzkris.common.core.utils.IpUtil;
import com.wzkris.message.feign.stafflog.StaffLogFeign;
import com.wzkris.message.feign.stafflog.req.StaffLoginLogReq;
import com.wzkris.message.feign.userlog.UserLogFeign;
import com.wzkris.message.feign.userlog.req.UserLoginLogReq;
import com.wzkris.principal.feign.customer.CustomerInfoFeign;
import com.wzkris.principal.feign.staff.StaffInfoFeign;
import com.wzkris.principal.feign.user.UserInfoFeign;
import com.wzkris.principal.feign.user.req.LoginInfoReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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

    private final UserLogFeign userLogFeign;

    private final StaffLogFeign staffLogFeign;

    private final UserInfoFeign userInfoFeign;

    private final StaffInfoFeign staffInfoFeign;

    private final CustomerInfoFeign customerInfoFeign;

    @Async
    @EventListener
    public void loginEvent(LoginEvent event) {
        final MyPrincipal principal = event.getPrincipal();
        log.info("'{}' 发生登录事件", principal);

        if (Objects.equals(principal.getType(), AuthType.USER)) {
            this.handleLoginUser(event, (LoginUser) principal);
        } else if (Objects.equals(principal.getType(), AuthType.STAFF)) {
            this.handleLoginStaff(event, (LoginStaff) principal);
        } else if (Objects.equals(principal.getType(), AuthType.CUSTOMER)) {
            this.handleLoginCustomer(event, (LoginCustomer) principal);
        }
    }

    private void handleLoginUser(LoginEvent event, LoginUser user) {
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

            tokenService.putSession(user.getId(), event.getRefreshToken(), onlineSession);

            LoginInfoReq loginInfoReq = new LoginInfoReq(user.getId());
            loginInfoReq.setLoginIp(ipAddr);
            loginInfoReq.setLoginDate(new Date());
            userInfoFeign.updateLoginInfo(loginInfoReq);
        }
        // 插入后台登陆日志
        final UserLoginLogReq loginLogReq = new UserLoginLogReq();
        loginLogReq.setUserId(user.getId());
        loginLogReq.setUsername(user.getUsername());
        loginLogReq.setLoginTime(new Date());
        loginLogReq.setLoginIp(ipAddr);
        loginLogReq.setLoginType(loginType);
        loginLogReq.setSuccess(event.getSuccess());
        loginLogReq.setErrorMsg(errorMsg);
        loginLogReq.setLoginLocation(loginLocation);
        loginLogReq.setOs(userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME));
        loginLogReq.setBrowser(browser);
        userLogFeign.saveLoginlog(loginLogReq);
    }

    private void handleLoginStaff(LoginEvent event, LoginStaff staff) {
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

            tokenService.putSession(staff.getId(), event.getRefreshToken(), onlineSession);

            LoginInfoReq loginInfoReq = new LoginInfoReq(staff.getId());
            loginInfoReq.setLoginIp(ipAddr);
            loginInfoReq.setLoginDate(new Date());
            staffInfoFeign.updateLoginInfo(loginInfoReq);
        }
        // 插入后台登陆日志
        final StaffLoginLogReq loginLogReq = new StaffLoginLogReq();
        loginLogReq.setStaffId(staff.getId());
        loginLogReq.setStaffName(staff.getStaffName());
        loginLogReq.setTenantId(staff.getTenantId());
        loginLogReq.setLoginTime(new Date());
        loginLogReq.setLoginIp(ipAddr);
        loginLogReq.setLoginType(loginType);
        loginLogReq.setSuccess(event.getSuccess());
        loginLogReq.setErrorMsg(errorMsg);
        loginLogReq.setLoginLocation(loginLocation);
        loginLogReq.setOs(userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME));
        loginLogReq.setBrowser(browser);
        staffLogFeign.saveLoginlog(loginLogReq);
    }

    private void handleLoginCustomer(LoginEvent event, LoginCustomer customer) {
        if (event.getSuccess()) { // 更新用户登录信息
            LoginInfoReq loginInfoReq = new LoginInfoReq(customer.getId());
            loginInfoReq.setLoginIp(event.getIpAddr());
            loginInfoReq.setLoginDate(new Date());
            customerInfoFeign.updateLoginInfo(loginInfoReq);
        }
    }

}

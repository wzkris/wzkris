package com.wzkris.auth.listener;

import com.wzkris.auth.domain.OnlineUser;
import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.model.CorePrincipal;
import com.wzkris.common.core.model.domain.LoginCustomer;
import com.wzkris.common.core.model.domain.LoginStaff;
import com.wzkris.common.core.model.domain.LoginUser;
import com.wzkris.common.core.utils.IpUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.message.feign.stafflog.StaffLogFeign;
import com.wzkris.message.feign.stafflog.req.StaffLoginLogReq;
import com.wzkris.message.feign.userlog.UserLogFeign;
import com.wzkris.message.feign.userlog.req.UserLoginLogReq;
import com.wzkris.principal.feign.customer.CustomerInfoFeign;
import com.wzkris.principal.feign.staffinfo.StaffInfoFeign;
import com.wzkris.principal.feign.userinfo.UserInfoFeign;
import com.wzkris.principal.feign.userinfo.req.LoginInfoReq;
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

    private final StaffLogFeign staffLogFeign;

    private final UserInfoFeign userInfoFeign;

    private final StaffInfoFeign staffInfoFeign;

    private final CustomerInfoFeign customerInfoFeign;

    @Async
    @EventListener
    public void loginEvent(LoginEvent event) {
        final CorePrincipal principal = event.getPrincipal();
        log.info("'{}' 发生登录事件", principal);

        if (StringUtil.equals(principal.getType(), AuthType.USER.getValue())) {
            this.handleLoginUser(event, (LoginUser) principal);
        } else if (StringUtil.equals(principal.getType(), AuthType.STAFF.getValue())) {
            this.handleLoginStaff(event, (LoginStaff) principal);
        } else if (StringUtil.equals(principal.getType(), AuthType.CUSTOMER.getValue())) {
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
        final UserLoginLogReq loginLogReq = new UserLoginLogReq();
        loginLogReq.setUserId(user.getId());
        loginLogReq.setStaffName(user.getUsername());
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
            OnlineUser onlineUser = new OnlineUser();
            onlineUser.setDeviceType(userAgent.getValue(UserAgent.DEVICE_NAME));
            onlineUser.setLoginIp(ipAddr);
            onlineUser.setLoginLocation(loginLocation);
            onlineUser.setBrowser(browser);
            onlineUser.setOs(userAgent.getValue(UserAgent.OPERATING_SYSTEM_NAME));
            onlineUser.setLoginTime(new Date());

            tokenService.putOnlineSession(staff.getId(), event.getRefreshToken(), onlineUser);

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

    private void handleLoginCustomer(LoginEvent event, LoginCustomer user) {
        if (event.getSuccess()) { // 更新用户登录信息
            LoginInfoReq loginInfoReq = new LoginInfoReq(user.getId());
            loginInfoReq.setLoginIp(event.getIpAddr());
            loginInfoReq.setLoginDate(new Date());
            customerInfoFeign.updateLoginInfo(loginInfoReq);
        }
    }

}

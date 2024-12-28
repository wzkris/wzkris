package com.wzkris.auth.listener;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.useragent.UserAgent;
import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.utils.AddressUtil;
import com.wzkris.common.security.oauth2.domain.AuthBaseUser;
import com.wzkris.common.security.oauth2.domain.model.ClientUser;
import com.wzkris.common.security.oauth2.domain.model.LoginUser;
import com.wzkris.common.security.oauth2.enums.LoginType;
import com.wzkris.system.api.RemoteLogApi;
import com.wzkris.system.api.domain.request.LoginLogReq;
import com.wzkris.user.api.RemoteAppUserApi;
import com.wzkris.user.api.RemoteSysUserApi;
import com.wzkris.user.api.domain.request.LoginInfoReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
    private final RemoteLogApi remoteLogApi;
    private final RemoteSysUserApi remoteSysUserApi;
    private final RemoteAppUserApi remoteAppUserApi;

    /**
     * 登录事件
     **/
    @Async
    @EventListener
    public void loginEvent(LoginEvent event) {
        final AuthBaseUser baseUser = event.getUser();
        final String ipAddr = event.getIpAddr();
        final UserAgent userAgent = event.getUserAgent();
        log.info("监听到用户’{}‘登录成功事件, 登录IP：{}", baseUser.getName(), ipAddr);

        if (ObjUtil.equals(baseUser.getLoginType(), LoginType.SYSTEM_USER)) {
            LoginUser loginUser = (LoginUser) baseUser;
            // 更新用户登录信息
            LoginInfoReq loginInfoReq = new LoginInfoReq(loginUser.getUserId());
            loginInfoReq.setLoginIp(ipAddr);
            loginInfoReq.setLoginDate(DateUtil.current());
            remoteSysUserApi.updateLoginInfo(loginInfoReq);
            // 插入后台登陆日志
            final LoginLogReq loginLogReq = new LoginLogReq();
            loginLogReq.setUsername(loginUser.getUsername());
            loginLogReq.setTenantId(loginUser.getTenantId());
            loginLogReq.setLoginTime(DateUtil.current());
            loginLogReq.setLoginIp(ipAddr);
            loginLogReq.setStatus(CommonConstants.STATUS_ENABLE);
            loginLogReq.setLoginLocation(AddressUtil.getRealAddressByIp(ipAddr));
            // 获取客户端操作系统
            String os = userAgent.getOs().getName();
            // 获取客户端浏览器
            String browser = userAgent.getBrowser().getName();
            loginLogReq.setOs(os);
            loginLogReq.setBrowser(browser);
            remoteLogApi.insertLoginlog(loginLogReq);
        }
        else if (ObjUtil.equals(baseUser.getLoginType(), LoginType.CLIENT_USER)) {
            ClientUser clientUser = (ClientUser) baseUser;
            // 更新用户登录信息
            LoginInfoReq loginInfoReq = new LoginInfoReq(clientUser.getUserId());
            loginInfoReq.setLoginIp(ipAddr);
            loginInfoReq.setLoginDate(DateUtil.current());
            remoteAppUserApi.updateLoginInfo(loginInfoReq);
        }
    }

}

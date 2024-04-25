package com.thingslink.auth.listening;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.useragent.UserAgent;
import com.thingslink.auth.listening.event.UserLoginEvent;
import com.thingslink.common.core.utils.ip.AddressUtil;
import com.thingslink.common.security.model.LoginUser;
import com.thingslink.common.security.model.LoginAppUser;
import com.thingslink.common.security.model.LoginSysUser;
import com.thingslink.common.security.utils.CurrentUserHolder;
import com.thingslink.system.api.RemoteLogApi;
import com.thingslink.system.api.domain.LoginLogDTO;
import com.thingslink.user.api.RemoteAppUserApi;
import com.thingslink.user.api.RemoteSysUserApi;
import com.thingslink.user.api.domain.dto.LoginInfoDTO;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class LoginEventListener {
    private final RemoteLogApi remoteLogApi;
    private final RemoteSysUserApi remoteSysUserApi;
    private final RemoteAppUserApi remoteAppUserApi;

    /**
     * 异步记录登录日志
     **/
    @Async
    @EventListener
    public void recordLoginLog(UserLoginEvent userLoginEvent) {
        log.info(Thread.currentThread().getName() + "监听到事件：" + userLoginEvent);
        final LoginUser loginUser = CurrentUserHolder.getPrincipal();
        final String ip = userLoginEvent.getIp();
        final UserAgent userAgent = userLoginEvent.getUserAgent();

        if (loginUser instanceof LoginSysUser sysUser) {
            // 更新用户登录信息
            LoginInfoDTO loginInfoDTO = new LoginInfoDTO();
            loginInfoDTO.setUserId(sysUser.getUserId());
            loginInfoDTO.setLoginIp(ip);
            loginInfoDTO.setLoginDate(DateUtil.current());
            remoteSysUserApi.updateLoginInfo(loginInfoDTO);
            // 插入后台登陆日志
            final LoginLogDTO loginLogDTO = new LoginLogDTO();
            loginLogDTO.setUserId(sysUser.getUserId());
            loginLogDTO.setLoginTime(DateUtil.current());
            loginLogDTO.setIpAddr(ip);
            loginLogDTO.setAddress(AddressUtil.getRealAddressByIp(ip));
            // 获取客户端操作系统
            String os = userAgent.getOsVersion();
            // 获取客户端浏览器
            String browser = userAgent.getBrowser().getName();
            loginLogDTO.setOs(os);
            loginLogDTO.setBrowser(browser);
            remoteLogApi.insertLoginlog(loginLogDTO);
        }
        else if (loginUser instanceof LoginAppUser appUser) {
            // 更新用户登录信息
            LoginInfoDTO loginInfoDTO = new LoginInfoDTO();
            loginInfoDTO.setUserId(appUser.getUserId());
            loginInfoDTO.setLoginIp(ip);
            loginInfoDTO.setLoginDate(DateUtil.current());
            remoteAppUserApi.updateLoginInfo(loginInfoDTO);
        }

    }
}

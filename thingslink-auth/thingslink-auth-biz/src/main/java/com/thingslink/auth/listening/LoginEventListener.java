package com.thingslink.auth.listening;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.useragent.UserAgent;
import com.thingslink.auth.listening.event.UserLoginEvent;
import com.thingslink.common.core.utils.ip.AddressUtil;
import com.thingslink.common.security.oauth2.constants.OAuth2Type;
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
    public void recordLoginLog(UserLoginEvent event) {
        log.info(Thread.currentThread().getName() + "监听到事件：" + event);
        if (event.getUserId() == null) {
            log.warn("用户id为空，不进行登录日志记录，事件信息：{}", event);
            return;
        }
        final String oauth2Type = event.getOauth2Type();
        final String ip = event.getIp();
        final UserAgent userAgent = event.getUserAgent();

        if (oauth2Type.equals(OAuth2Type.SYS_USER.getValue())) {
            // 更新用户登录信息
            LoginInfoDTO loginInfoDTO = new LoginInfoDTO();
            loginInfoDTO.setUserId(event.getUserId());
            loginInfoDTO.setLoginIp(ip);
            loginInfoDTO.setLoginDate(DateUtil.current());
            remoteSysUserApi.updateLoginInfo(loginInfoDTO);
            // 插入后台登陆日志
            final LoginLogDTO loginLogDTO = new LoginLogDTO();
            loginLogDTO.setUserId(event.getUserId());
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
        else if (oauth2Type.equals(OAuth2Type.APP_USER.getValue())) {
            // 更新用户登录信息
            LoginInfoDTO loginInfoDTO = new LoginInfoDTO();
            loginInfoDTO.setUserId(event.getUserId());
            loginInfoDTO.setLoginIp(ip);
            loginInfoDTO.setLoginDate(DateUtil.current());
            remoteAppUserApi.updateLoginInfo(loginInfoDTO);
        }

    }
}

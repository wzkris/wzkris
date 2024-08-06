package com.wzkris.auth.listener;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.useragent.UserAgent;
import com.wzkris.auth.constant.KeyConstants;
import com.wzkris.auth.listener.event.UserLoginEvent;
import com.wzkris.auth.listener.event.UserLogoutEvent;
import com.wzkris.common.core.utils.ip.AddressUtil;
import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.common.security.oauth2.constants.OAuth2Type;
import com.wzkris.common.security.oauth2.domain.model.LoginApper;
import com.wzkris.common.security.oauth2.domain.model.LoginSyser;
import com.wzkris.system.api.RemoteLogApi;
import com.wzkris.system.api.domain.LoginLogDTO;
import com.wzkris.user.api.RemoteAppUserApi;
import com.wzkris.user.api.RemoteSysUserApi;
import com.wzkris.user.api.domain.dto.LoginInfoDTO;
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
     * 登录成功事件
     **/
    @Async
    @EventListener
    public void loginSuccess(UserLoginEvent event) {
        log.info("监听到登录事件：{}", event);
        final String oauth2Type = event.getOauth2Type();
        final String ipAddr = event.getIpAddr();
        final UserAgent userAgent = event.getUserAgent();

        if (oauth2Type.equals(OAuth2Type.SYS_USER.getValue())) {
            Long userId = ((LoginSyser) event.getLoginer()).getUserId();
            // 更新用户登录信息
            LoginInfoDTO loginInfoDTO = new LoginInfoDTO();
            loginInfoDTO.setUserId(userId);
            loginInfoDTO.setLoginIp(ipAddr);
            loginInfoDTO.setLoginDate(DateUtil.current());
            remoteSysUserApi.updateLoginInfo(loginInfoDTO);
            // 插入后台登陆日志
            final LoginLogDTO loginLogDTO = new LoginLogDTO();
            loginLogDTO.setUserId(userId);
            loginLogDTO.setLoginTime(DateUtil.current());
            loginLogDTO.setIpAddr(ipAddr);
            loginLogDTO.setAddress(AddressUtil.getRealAddressByIp(ipAddr));
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
            loginInfoDTO.setUserId(((LoginApper) event.getLoginer()).getUserId());
            loginInfoDTO.setLoginIp(ipAddr);
            loginInfoDTO.setLoginDate(DateUtil.current());
            remoteAppUserApi.updateLoginInfo(loginInfoDTO);
        }
    }

    /**
     * 登出成功事件
     **/
    @Async
    @EventListener
    public void logoutSuccess(UserLogoutEvent event) {
        log.info("监听到登出事件：{}", event);
        final String oauth2Type = event.getOauth2Type();
        final String ipAddr = event.getIpAddr();
        final UserAgent userAgent = event.getUserAgent();
        if (oauth2Type.equals(OAuth2Type.SYS_USER.getValue())) {
            LoginSyser loginer = (LoginSyser) event.getLoginer();
            RedisUtil.delObj(String.format(KeyConstants.LOGIN_USER_ROUTER, loginer.getUserId()));
        }
        else if (oauth2Type.equals(OAuth2Type.APP_USER.getValue())) {

        }
    }
}

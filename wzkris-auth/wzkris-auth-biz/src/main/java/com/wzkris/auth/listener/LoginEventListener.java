package com.wzkris.auth.listener;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.useragent.UserAgent;
import com.wzkris.auth.constant.KeyConstants;
import com.wzkris.auth.listener.event.LoginFailEvent;
import com.wzkris.auth.listener.event.LoginSuccessEvent;
import com.wzkris.auth.listener.event.UserLogoutEvent;
import com.wzkris.common.core.constant.CommonConstants;
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
     * 登录成功事件
     **/
    @Async
    @EventListener
    public void loginSuccess(LoginSuccessEvent event) {
        log.info("监听到登录成功事件：{}", event);
        final String oauth2Type = event.getOauth2Type();
        final String ipAddr = event.getIpAddr();
        final UserAgent userAgent = event.getUserAgent();

        if (oauth2Type.equals(OAuth2Type.SYS_USER.getValue())) {
            LoginSyser loginSyser = (LoginSyser) event.getLoginer();
            // 更新用户登录信息
            LoginInfoDTO loginInfoDTO = new LoginInfoDTO();
            loginInfoDTO.setUserId(loginSyser.getUserId());
            loginInfoDTO.setLoginIp(ipAddr);
            loginInfoDTO.setLoginDate(DateUtil.current());
            remoteSysUserApi.updateLoginInfo(loginInfoDTO);
            // 插入后台登陆日志
            final LoginLogDTO loginLogDTO = new LoginLogDTO();
            loginLogDTO.setUsername(loginSyser.getUsername());
            loginLogDTO.setLoginTime(DateUtil.current());
            loginLogDTO.setLoginIp(ipAddr);
            loginLogDTO.setStatus(CommonConstants.STATUS_ENABLE);
            loginLogDTO.setLoginLocation(AddressUtil.getRealAddressByIp(ipAddr));
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
     * 登录失败事件
     **/
    @Async
    @EventListener
    public void loginFail(LoginFailEvent event) {
        log.info("监听到登录失败事件：{}", event);
        final String oauth2Type = event.getOauth2Type();
        final String ipAddr = event.getIpAddr();
        final UserAgent userAgent = event.getUserAgent();
        if (oauth2Type.equals(OAuth2Type.SYS_USER.getValue())) {
            // 插入后台登陆日志
            final LoginLogDTO loginLogDTO = new LoginLogDTO();
            loginLogDTO.setUsername(event.getUsername());
            loginLogDTO.setLoginTime(DateUtil.current());
            loginLogDTO.setLoginIp(ipAddr);
            loginLogDTO.setStatus(CommonConstants.STATUS_DISABLE);
            loginLogDTO.setLoginLocation(AddressUtil.getRealAddressByIp(ipAddr));
            // 获取客户端操作系统
            String os = userAgent.getOsVersion();
            // 获取客户端浏览器
            String browser = userAgent.getBrowser().getName();
            loginLogDTO.setOs(os);
            loginLogDTO.setBrowser(browser);
            remoteLogApi.insertLoginlog(loginLogDTO);
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
        if (oauth2Type.equals(OAuth2Type.SYS_USER.getValue())) {
            LoginSyser loginer = (LoginSyser) event.getLoginer();
            RedisUtil.delObj(String.format(KeyConstants.LOGIN_USER_ROUTER, loginer.getUserId()));
        }
    }
}
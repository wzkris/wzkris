package com.wzkris.auth.listener;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.useragent.UserAgent;
import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.utils.ip.AddressUtil;
import com.wzkris.common.security.oauth2.domain.WzUser;
import com.wzkris.common.security.oauth2.domain.model.LoginApper;
import com.wzkris.common.security.oauth2.domain.model.LoginSyser;
import com.wzkris.common.security.oauth2.enums.UserType;
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
     * 登录事件
     **/
    @Async
    @EventListener
    public void loginEvent(LoginEvent event) {
        final WzUser wzUser = event.getWzUser();
        final String ipAddr = event.getIpAddr();
        final UserAgent userAgent = event.getUserAgent();
        log.info("监听到用户’{}‘登录成功事件, 登录IP：{}", wzUser.getName(), ipAddr);

        if (ObjUtil.equals(wzUser.getUserType(), UserType.SYS_USER)) {
            LoginSyser loginSyser = (LoginSyser) wzUser.getPrincipal();
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
            String os = userAgent.getOs().getName();
            // 获取客户端浏览器
            String browser = userAgent.getBrowser().getName();
            loginLogDTO.setOs(os);
            loginLogDTO.setBrowser(browser);
            remoteLogApi.insertLoginlog(loginLogDTO);
        }
        else if (ObjUtil.equals(wzUser.getUserType(), UserType.APP_USER)) {
            // 更新用户登录信息
            LoginInfoDTO loginInfoDTO = new LoginInfoDTO();
            loginInfoDTO.setUserId(((LoginApper) wzUser.getPrincipal()).getUserId());
            loginInfoDTO.setLoginIp(ipAddr);
            loginInfoDTO.setLoginDate(DateUtil.current());
            remoteAppUserApi.updateLoginInfo(loginInfoDTO);
        }
    }

}

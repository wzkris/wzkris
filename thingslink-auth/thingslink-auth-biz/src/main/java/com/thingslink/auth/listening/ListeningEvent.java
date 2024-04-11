package com.thingslink.auth.listening;

import com.thingslink.auth.domain.Customer;
import com.thingslink.auth.domain.SysUser;
import com.thingslink.auth.listening.event.UserLoginEvent;
import com.thingslink.auth.mapper.CustomerMapper;
import com.thingslink.auth.mapper.SysUserMapper;
import com.thingslink.common.core.utils.ServletUtil;
import com.thingslink.common.core.utils.ip.AddressUtil;
import com.thingslink.common.security.model.AbstractUser;
import com.thingslink.common.security.model.AppUser;
import com.thingslink.common.security.model.LoginUser;
import com.thingslink.system.api.RemoteLogApi;
import com.thingslink.system.api.domain.LoginLogDTO;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 监听事件
 * @date : 2023/8/28 10:05
 */
@Slf4j
@Component
@AllArgsConstructor
public class ListeningEvent {
    private final RemoteLogApi remoteLogApi;
    private final SysUserMapper sysUserMapper;
    private final CustomerMapper customerMapper;

    /**
     * 异步记录登录日志
     **/
    @Async
    @EventListener
    public void recordLoginLog(UserLoginEvent userLoginEvent) {
        log.info(Thread.currentThread().getName() + "监听到事件：" + userLoginEvent);
        final AbstractUser userInfo = userLoginEvent.getUserInfo();
        final HttpServletRequest request = userLoginEvent.getRequest();
        final String ip = ServletUtil.getClientIP(request);

        if (userInfo instanceof LoginUser loginUser) {
            // 更新用户登录信息
            SysUser user = new SysUser(loginUser.getUserId());
            user.setLoginIp(ip);
            user.setLoginDate(LocalDateTime.now());
            sysUserMapper.updateById(user);
            // 插入后台登陆日志
            final LoginLogDTO loginLogDTO = new LoginLogDTO();
            loginLogDTO.setUserId(loginUser.getUserId());
            loginLogDTO.setLoginTime(LocalDateTime.now());
            loginLogDTO.setIpAddr(ip);
            loginLogDTO.setAddress(AddressUtil.getRealAddressByIp(ip));
            final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            // 获取客户端操作系统
            String os = userAgent.getOperatingSystem().getName();
            // 获取客户端浏览器
            String browser = userAgent.getBrowser().getName();
            loginLogDTO.setOs(os);
            loginLogDTO.setBrowser(browser);
            remoteLogApi.insertLoginlog(loginLogDTO);
        }
        else if (userInfo instanceof AppUser appUser) {
            // 更新用户登录信息
            Customer customer = new Customer(appUser.getUserId());
            customer.setLoginIp(ip);
            customer.setLoginDate(LocalDateTime.now());
            customerMapper.updateById(customer);
        }

    }
}

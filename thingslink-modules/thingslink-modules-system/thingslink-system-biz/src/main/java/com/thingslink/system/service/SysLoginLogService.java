package com.thingslink.system.service;

import com.thingslink.system.domain.SysLoginLog;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 登录日志
 * @date : 2024/1/10 13:55
 */
public interface SysLoginLogService {
    List<SysLoginLog> list(SysLoginLog loginLog);
}

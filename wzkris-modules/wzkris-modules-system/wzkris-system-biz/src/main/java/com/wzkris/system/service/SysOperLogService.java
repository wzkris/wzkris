package com.wzkris.system.service;

import com.wzkris.system.domain.SysOperLog;

import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author wzkris
 */
public interface SysOperLogService {
    List<SysOperLog> list(SysOperLog sysOperLog);
}

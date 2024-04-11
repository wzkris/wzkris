package com.thingslink.system.service;

import com.thingslink.system.domain.OperLog;

import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author wzkris
 */
public interface OperLogService {
    List<OperLog> list(OperLog operLog);
}

package com.thingslink.system.service;


import com.thingslink.system.domain.SysJobLog;

import java.util.List;

/**
 * 定时任务调度日志信息信息 服务层
 *
 * @author wzkris
 */
public interface SysJobLogService {
    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param sysJobLog 调度日志信息
     * @return 调度任务日志集合
     */
    List<SysJobLog> list(SysJobLog sysJobLog);

}

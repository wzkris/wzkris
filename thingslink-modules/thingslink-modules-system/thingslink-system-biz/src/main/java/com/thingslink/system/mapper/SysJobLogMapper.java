package com.thingslink.system.mapper;


import com.thingslink.common.orm.plus.BaseMapperPlus;
import com.thingslink.system.domain.SysJobLog;

import java.util.List;

/**
 * 调度任务日志信息 数据层
 *
 * @author wzkris
 */
public interface SysJobLogMapper extends BaseMapperPlus<SysJobLog> {
    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param sysJobLog 调度日志信息
     * @return 调度任务日志集合
     */
    List<SysJobLog> list(SysJobLog sysJobLog);

    /**
     * 清空任务日志
     */
    void cleanJobLog();
}

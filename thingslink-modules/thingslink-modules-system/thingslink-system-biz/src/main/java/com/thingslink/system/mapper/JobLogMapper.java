package com.thingslink.system.mapper;


import com.thingslink.common.orm.plus.BaseMapperPlus;
import com.thingslink.system.domain.JobLog;

import java.util.List;

/**
 * 调度任务日志信息 数据层
 *
 * @author wzkris
 */
public interface JobLogMapper extends BaseMapperPlus<JobLog> {
    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    List<JobLog> list(JobLog jobLog);

    /**
     * 清空任务日志
     */
    void cleanJobLog();
}

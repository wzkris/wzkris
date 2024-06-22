package com.thingslink.system.mapper;


import com.thingslink.common.orm.plus.BaseMapperPlus;
import com.thingslink.system.domain.SysJobLog;

/**
 * 调度任务日志信息 数据层
 *
 * @author wzkris
 */
public interface SysJobLogMapper extends BaseMapperPlus<SysJobLog> {

    /**
     * 清空任务日志
     */
    void cleanJobLog();
}

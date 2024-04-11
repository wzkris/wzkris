package com.thingslink.system.mapper;


import com.thingslink.common.orm.plus.BaseMapperPlus;
import com.thingslink.system.domain.SysJob;

import java.util.List;

/**
 * 调度任务信息 数据层
 *
 * @author wzkris
 */
public interface SysJobMapper extends BaseMapperPlus<SysJob> {
    /**
     * 查询调度任务日志集合
     *
     * @param sysJob 调度信息
     * @return 操作日志集合
     */
    List<SysJob> list(SysJob sysJob);

}
